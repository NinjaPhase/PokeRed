package com.ninjaphase.pokered.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     The {@code DataManager} handles the data within the game.
 * </p>
 */
public class DataManager implements Disposable {
    private static final JsonReader READER = new JsonReader();

    private Texture speciesTexture, texSpeciesBack;
    private Species[] species;
    private final JsonValue baseData;

    private final List<TileMap> maps;
    private final Map<String, TileMap> mapsByName;
    private final Map<String, Move> moves;
    private Texture tiles;

    /**
     * <p>
     *     Constructs a new {@code DataManager}.
     * </p>
     */
    public DataManager() {
        this.maps = new ArrayList<>();
        this.mapsByName = new HashMap<>();
        this.moves = new HashMap<>();
        this.baseData = READER.parse(Gdx.files.internal("data/data.base.json"));
    }

    /**
     * <p>
     *     Loads the map data.
     * </p>
     */
    public void loadMaps() {
        this.tiles = new Texture("img/tiles.png");
        for(int i = 0; i < this.baseData.get("maps").size; i++) {
            TileMap map = new TileMap(this.tiles,
                    READER.parse(Gdx.files.internal("data/maps/" + this.baseData.get("maps").getString(i))));
            this.maps.add(map);
            this.mapsByName.put(map.getInternalName(), map);
        }
    }

    /**
     * <p>
     *     Loads the move data.
     * </p>
     */
    public void loadMoves() {
        JsonValue moveData = READER.parse(Gdx.files.internal("data/data.move.json"));
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
        JsonValue pokemonData = READER.parse(Gdx.files.internal("data/data.pokemon.json"));
        this.species = new Species[pokemonData.size];
        this.speciesTexture = new Texture("img/pokemon.png");
        this.texSpeciesBack = new Texture("img/pokemon_back.png");
        int x = 0, y = 0;
        for(int i = 0; i < pokemonData.size; i++) {
            JsonValue data = pokemonData.get(i);
            this.species[i] = new Species(Integer.parseInt(data.name), data,
                    new TextureRegion(this.speciesTexture, x, y, 64, 64),
                    new TextureRegion(this.texSpeciesBack, x, y, 64, 64));
            x += 64;
            if(x >= this.speciesTexture.getWidth()) {
                x = 0;
                y += 64;
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
     * @return The starting map.
     */
    public TileMap getStartMap() {
        return this.mapsByName.get(this.baseData.getString("start_map"));
    }

    /**
     * @param internalName The internal name of the map.
     * @return The map if it exists, otherwise {@code null}.
     */
    public TileMap getMap(String internalName) {
        return this.mapsByName.get(internalName);
    }

    /**
     * @param internalName The internal name of the move.
     * @return The move if it exists, otherwise {@code null}.
     */
    public Move getMove(String internalName) {
        return this.moves.get(internalName);
    }

    @Override
    public void dispose() {
        if(this.tiles != null) {
            this.tiles.dispose();
            this.tiles = null;
        }
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
