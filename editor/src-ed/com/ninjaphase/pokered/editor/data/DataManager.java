package com.ninjaphase.pokered.editor.data;

import com.ninjaphase.pokered.editor.data.listeners.MapChangeListener;
import com.ninjaphase.pokered.editor.data.listeners.StoryChangeListener;
import com.ninjaphase.pokered.editor.data.story.Story;
import com.ninjaphase.pokered.editor.data.story.tilemap.TileMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *     The {@code DataManager} handles data changes within the application.
 * </p>
 */
public class DataManager {

    private Story story;
    private TileMap editingMap;
    private List<StoryChangeListener> storyChangeListeners;
    private List<MapChangeListener> mapChangeListeners;

    /**
     * <p>
     *     Constructs a new {@code DataManager}.
     * </p>
     */
    public DataManager() {
        this.storyChangeListeners = new LinkedList<>();
        this.mapChangeListeners = new LinkedList<>();
    }

    /**
     * <p>
     *     Sets the tile map.
     * </p>
     *
     * @param t The tile map.
     */
    public void setTileMap(TileMap t) {
        for(MapChangeListener mcl : this.mapChangeListeners) {
            mcl.onMapChange(t);
        }
        this.editingMap = t;
    }

    /**
     * <p>
     *     Sets the story.
     * </p>
     *
     * @param s The story.
     */
    public void setStory(Story s) {
        this.setTileMap(null);
        for(StoryChangeListener scl : this.storyChangeListeners) {
            scl.onStoryChange(s);
        }
        this.story = s;
    }

    /**
     * <p>
     *     Adds a map change listener to the {@code DataManager}.
     * </p>
     *
     * @param listener The listener.
     */
    public void addMapChangeListener(MapChangeListener listener) {
        this.mapChangeListeners.add(listener);
    }

    /**
     * <p>
     *     Adds a story change listener to the {@code DataManager}.
     * </p>
     *
     * @param listener The listener.
     */
    public void addStoryChangeListener(StoryChangeListener listener) {
        this.storyChangeListeners.add(listener);
    }

    /**
     * @return The currently loaded story.
     */
    public Story getStory() {
        return this.story;
    }

}
