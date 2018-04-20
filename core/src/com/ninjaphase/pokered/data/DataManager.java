package com.ninjaphase.pokered.data;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.ninjaphase.pokered.data.map.EncounterData;
import com.ninjaphase.pokered.data.map.TileMap;
import com.ninjaphase.pokered.data.map.TileSet;
import com.ninjaphase.pokered.data.map.random.RandomTileMapData;
import com.ninjaphase.pokered.data.pokemon.Move;
import com.ninjaphase.pokered.data.pokemon.Species;
import com.ninjaphase.pokered.data.story.Story;
import com.ninjaphase.pokered.data.story.exception.StoryReadException;
import com.ninjaphase.pokered.data.story.io.ZipStoryReader;
import com.ninjaphase.pokered.util.ResourceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * <p>
 *     The {@code DataManager} handles the data within the game.
 * </p>
 */
public class DataManager implements Disposable {
    private static final int MAX_IMAGE_SIZE = 512*1024;
    private static final JsonReader READER = new JsonReader();
    private static final String BASE_DATA = "data/data.base.json",
                                MOVE_DATA = "data/data.move.json",
                                SPECIES_DATA = "data/data.pokemon.json";

    private Texture speciesTexture, texSpeciesBack;
    private Species[] species;
    private final Map<String, Species> speciesByName;
    public final Map<String, EncounterData> randomSpeciesEncounters;
    public final JsonValue baseData;
    private final Map<String, Move> moves;
    private final List<Story> stories;
    private Story currentStory;
    private Story[] storyArray;

    private ResourceManager resourceManager;
    public final List<RandomTileMapData> randomTileMapDatas;

    /**
     * <p>
     *     Constructs a new {@code DataManager}.
     * </p>
     */
    public DataManager(ResourceManager rm) {
        this.randomSpeciesEncounters = new HashMap<>();
        this.randomSpeciesEncounters.put("common_grass", new EncounterData());
        this.randomSpeciesEncounters.put("rare_grass", new EncounterData());
        this.resourceManager = rm;
        this.speciesByName = new HashMap<>();
        this.moves = new HashMap<>();
        this.stories = new ArrayList<>();
        this.randomTileMapDatas = new ArrayList<>();
        this.baseData = READER.parse(Gdx.files.internal(DataManager.BASE_DATA));
    }

    /**
     * <p>
     *     Loads the move data.
     * </p>
     */
    public void loadMoves() {
        JsonValue moveData = READER.parse(Gdx.files.internal(DataManager.MOVE_DATA));
        for(int i = 0; i < moveData.size; i++) {
            Move move = new Move(moveData.get(i));
            this.moves.put(moveData.get(i).name, move);
        }
    }

    /**
     * <p>
     *     Loads the species data.
     * </p>
     */
    public void loadSpecies() {
        JsonValue pokemonData = READER.parse(Gdx.files.internal(DataManager.SPECIES_DATA));
        this.speciesByName.clear();
        this.species = new Species[pokemonData.size];
        this.speciesTexture = new Texture("img/pokemon/pokemon_front.png");
        this.texSpeciesBack = new Texture("img/pokemon/pokemon_back.png");
        int x = 0, y = 0;
        for(int i = 0; i < pokemonData.size; i++) {
            JsonValue data = pokemonData.get(i);
            this.species[i] = new Species(Integer.parseInt(data.name), data,
                    new TextureRegion(this.speciesTexture, x, y, 64, 64),
                    new TextureRegion(this.texSpeciesBack, x, y, 64, 64));
            if(data.has("random_encounter")) {
                JsonValue jv = data.get("random_encounter");
                for(JsonValue child : jv) {
                    this.randomSpeciesEncounters.get(child.name).addEncounter(i, 0, child.asFloat());
                }
            }
            this.speciesByName.put(this.species[i].getName(), this.species[i]);
            x += 64;
            if(x >= this.speciesTexture.getWidth()) {
                x = 0;
                y += 64;
            }
        }
        this.randomSpeciesEncounters.get("common_grass").normaliseWeights();
        this.randomSpeciesEncounters.get("rare_grass").normaliseWeights();
    }

    /**
     * <p>
     *     Loads the story data.
     * </p>
     */
    public void loadStories() {
        this.stories.clear();
        FileHandle[] stories;
        if(Gdx.app.getType() == Application.ApplicationType.Android) {
            stories = new FileHandle[baseData.get("internal_stories").size];
            for(int i = 0; i < stories.length; i++) {
                stories[i] = Gdx.files.internal(baseData.get("internal_stories").getString(i));
            }
        } else {
            stories = Gdx.files.local("./story/").list(".zip");
        }
        for(FileHandle storyHandle : stories) {
            try {
                Story s = new Story(storyHandle);
                this.stories.add(s);
            } catch (StoryReadException e) {
                System.err.println("Unable to read story " + storyHandle);
            }
        }
        this.setCurrentStory(this.stories.get(0));
        this.storyArray = new Story[this.stories.size()];
        this.stories.toArray(this.storyArray);
    }

    /**
     * <p>
     *     Loads the random map data.
     * </p>
     */
    public void loadRandomMapData() {
        this.randomTileMapDatas.clear();
        FileHandle[] randomDataList;
        if(Gdx.app.getType() == Application.ApplicationType.Android) {
            randomDataList = new FileHandle[baseData.get("internal_random").size];
            for(int i = 0; i < randomDataList.length; i++) {
                randomDataList[i] = Gdx.files.internal(baseData.get("internal_random").getString(i));
            }
        } else {
            randomDataList = Gdx.files.local("./data/random/").list(".zip");
        }
        for(FileHandle randomData : randomDataList) {
            loadRandomMapZip(randomData);
        }
    }

    /**
     * <p>
     *     Loads the zips of random maps which will be displayed to the player.
     * </p>
     *
     * @param fileHandle The file handle.
     */
    private void loadRandomMapZip(FileHandle fileHandle) {
        ZipInputStream zis = null;
        List<JsonValue> tileSetJson = new ArrayList<>();
        List<JsonValue> tileMapJson = new ArrayList<>();
        Map<String, TileSet> tileSets = new HashMap<>();
        try {
            zis = new ZipInputStream(fileHandle.read());

            ZipEntry ze;
            while((ze = zis.getNextEntry()) != null) {
                String name = ze.getName();
                if(name.contains("/"))
                    name = name.substring(name.lastIndexOf('/'), name.length());
                if(name.startsWith("tileset.")) {
                    tileSetJson.add(ZipStoryReader.parseNoClose(zis));
                } else if(name.startsWith("maps.")) {
                    tileMapJson.add(ZipStoryReader.parseNoClose(zis));
                } else if(name.startsWith("graphic.")) {
                    byte[] bytes = new byte[MAX_IMAGE_SIZE];
                    int readBytes = 0;
                    while(true) {
                        int length = zis.read(bytes, readBytes, bytes.length - readBytes);
                        if(length == -1) break;
                        readBytes += length;
                    }

                    this.resourceManager.add(name, new Texture(new Pixmap(bytes, 0, readBytes)));
                }
            }

            for(JsonValue jv : tileSetJson) {
                tileSets.put(jv.getString("internal_name"), new TileSet(resourceManager, jv));
            }
            for(JsonValue jv : tileMapJson) {
                this.randomTileMapDatas.add(
                        new RandomTileMapData(tileSets.get(jv.getString("tileset")), jv));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(zis != null) {
                try {
                    zis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * <p>
     *     Gets a species with a given index.
     * </p>
     *
     * @param idx The index of the species.
     * @return The {@code Species} data.
     */
    public Species getSpecies(int idx) {
        return this.species[idx];
    }

    /**
     * @param name the name of the species.
     * @return The {@code Species} data.
     */
    public Species getSpecies(String name) {
        return this.speciesByName.get(name);
    }

    /**
     * <p>
     *     Gets a random species.
     * </p>
     *
     * @return The {@code Species} data.
     */
    public Species getRandomSpecies() {
        return this.species[(int) (Math.random() * this.species.length)];
    }

    /**
     * @param internalName The internal name of the move.
     * @return The move if it exists, otherwise {@code null}.
     */
    public Move getMove(String internalName) {
        return this.moves.get(internalName);
    }

    /**
     * <p>
     *     Sets the current story.
     * </p>
     *
     * @param story The current story.
     */
    public void setCurrentStory(Story story) {
        this.currentStory = story;
        try {
            ZipStoryReader.readFull(story);
        } catch (StoryReadException e) {
            e.printStackTrace();;
            this.currentStory = null;
        }
    }

    /**
     * @return The current story.
     */
    public Story getCurrentStory() {
        return this.currentStory;
    }

    /**
     * @return The stories loaded as an array.
     */
    public Story[] getStoriesAsArray() {
        return this.storyArray;
    }

    @Override
    public void dispose() {
        if(this.speciesTexture != null) {
            this.speciesTexture.dispose();
            this.speciesTexture = null;
        }
        if(this.texSpeciesBack != null) {
            this.texSpeciesBack.dispose();
            this.texSpeciesBack = null;
        }
    }

}
