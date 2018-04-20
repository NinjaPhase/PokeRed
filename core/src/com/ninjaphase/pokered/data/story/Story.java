package com.ninjaphase.pokered.data.story;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.ninjaphase.pokered.PokemonApplication;
import com.ninjaphase.pokered.data.map.TileMap;
import com.ninjaphase.pokered.data.story.exception.StoryReadException;
import com.ninjaphase.pokered.data.story.io.ZipStoryReader;
import com.ninjaphase.pokered.entity.Player;
import com.ninjaphase.pokered.util.ResourceManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     A {@code Story} is a game which is defined as a zip file, this contains the maps, events and extra
 *     data required for a full Pokémon game.
 * </p>
 *
 * @author NinjaPhase
 * @version 20/03/2018
 */
public class Story implements Disposable {

    private ResourceManager storyResources;
    private FileHandle fileHandle;

    private StoryInformation information;
    private Map<String, TileMap> tileMaps;
    private Player player;

    private boolean isLoaded;
    /**
     * <p>
     *     Private constructor to initialise values.
     * </p>
     */
    private Story() {
        this.storyResources = new ResourceManager(PokemonApplication.getApplication().getResourceManager());
        this.tileMaps = new HashMap<>();
    }

    /**
     * <p>
     *     Constructs a new {@code Story} from a file.
     * </p>
     *
     * @param f The file handle.
     * @throws StoryReadException Thrown if there is an error reading the story.
     */
    public Story(FileHandle f) throws StoryReadException {
        this();
        this.fileHandle = f;
        this.information = ZipStoryReader.readGeneralData(this.fileHandle);
    }

    @Override
    public void dispose() {

    }

    /**
     * <p>
     *     Sets the story to loaded.
     * </p>
     * @param loaded Whether the story is loaded.
     */
    public void setLoaded(boolean loaded) {
        this.isLoaded = loaded;
    }

    /**
     * @return Whether the {@code Story} has been loaded.
     */
    public boolean isLoaded() {
        return this.isLoaded;
    }

    /**
     * @return The {@code ResourceManager} for the {@code Story}.
     */
    public ResourceManager getResourceManager() {
        return this.storyResources;
    }

    /**
     * @return The {@code FileHandle} of the {@code Story}.
     */
    public FileHandle getFileHandle() {
        return this.fileHandle;
    }

    /**
     * <p>
     *     Adds a map to the story.
     * </p>
     *
     * @param map The map to add.
     */
    public void addMap(TileMap map) {
        this.tileMaps.put(map.getInternalName(), map);
    }

    /**
     * @return The starting map of the {@code Story}.
     */
    public TileMap getStartMap() {
        return this.getMap(getInfo().startMapName);
    }

    /**
     * @return The players graphic.
     */
    public Texture getPlayerGraphic() {
        return this.getResourceManager().get(Texture.class, getInfo().playerGraphic);
    }

    /**
     * @param internalName The internal name
     * @return The map with the given internal name.
     */
    public TileMap getMap(String internalName) {
        return this.tileMaps.get(internalName);
    }

    /**
     * @param player The player.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * @return The player.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * @return The maps within the {@code Story}.
     */
    public Collection<TileMap> getMaps() {
        return this.tileMaps.values();
    }

    /**
     * @return The stories information.
     */
    public StoryInformation getInfo() {
        return this.information;
    }

    @Override
    public String toString() {
        return getInfo().name;
    }

}
