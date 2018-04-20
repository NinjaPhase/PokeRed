package com.ninjaphase.pokered.editor.data.story;

import com.ninjaphase.pokered.editor.data.story.tilemap.TileMap;
import com.ninjaphase.pokered.editor.data.story.tilemap.TileSet;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     The {@code DatabaseManager} is used to manage the database within a story.
 * </p>
 *
 * @author NinjaPhase
 */
public class DatabaseManager {

    private String storyName;
    private String storyDescription;
    private List<TileMap> rootMaps;
    private List<TileSet> tileSetList;

    /**
     * <p>
     *     Constructs a new {@code DatabaseManager}.
     * </p>
     */
    DatabaseManager() {
        this.rootMaps = new ArrayList<>();
        this.tileSetList = new ArrayList<>();
        this.storyName = "";
        this.storyDescription = "";
    }

    /**
     * @return The next map id.
     */
    public int getNextMapId() {
        int id = 1;
        for(TileMap m : this.rootMaps) {
            id = getNextMapId(id, m);
        }
        return id;
    }

    /**
     * @param m The map to get the next map id for.
     * @return The map id.
     */
    private int getNextMapId(int id, TileMap m) {
        if(m.getId() == id)
            id++;
        for(TileMap child : m.getChildren()) {
            id = getNextMapId(id, child);
        }
        return id;
    }

    /**
     * @param id The id of the tileset.
     * @return The tileset with the given id, if it doesn't exist then it is {@code null}.
     */
    public TileSet getTileSet(int id) {
        for(TileSet ts : this.tileSetList) {
            if(ts.getId() == id)
                return ts;
        }
        return null;
    }

    /**
     * <p>
     *     Sets the name of the story.
     * </p>
     *
     * @param storyName The new story name.
     */
    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    /**
     * <p>
     *     Gets the story name.
     * </p>
     */
    public String getStoryName() {
        return this.storyName;
    }

    /**
     * <p>
     *     Sets the story description.
     * </p>
     *
     * @param newValue The new value.
     */
    public void setStoryDescription(String newValue) {
        this.storyDescription = newValue;
    }

    /**
     * @return Gets the story description.
     */
    public String getStoryDescription() {
        return this.storyDescription;
    }

    /**
     * @return The list of top level maps.
     */
    public List<TileMap> getRootMaps() {
        return this.rootMaps;
    }

    /**
     * @return The list of tilesets.
     */
    public List<TileSet> getTileSets() {
        return this.tileSetList;
    }

    /**
     * @return The details as a JSON object.
     * @throws JSONException The json exception.
     */
    public JSONObject getDetailsAsJSON() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("name", this.getStoryName());
        return obj;
    }

}
