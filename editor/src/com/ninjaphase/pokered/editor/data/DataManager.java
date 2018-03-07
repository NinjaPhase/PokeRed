package com.ninjaphase.pokered.editor.data;

import com.ninjaphase.pokered.editor.PokeredEditor;
import com.ninjaphase.pokered.editor.data.listeners.MapChangeListener;
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

    private TileMap editingMap;
    private List<MapChangeListener> mapChangeListeners;

    /**
     * <p>
     *     Constructs a new {@code DataManager}.
     * </p>
     */
    public DataManager() {
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
     *     Sets the tile map from a file.
     * </p>
     *
     * @param f The tilemap file.
     */
    public void loadTileMap(File f) {
        if(!f.exists())
            return;
        TileMap t = null;
        FileReader fr;
        try {
            fr = new FileReader(f);
            t = new TileMap(new JSONObject(new JSONTokener(fr)));
            t.setFile(f);
            fr.close();
        } catch (JSONException | IOException exc) {
            System.err.println("Unable to load file: " + f.getName());
        }
        if(t == null)
            return;
        PokeredEditor.getEditor().getPreferences().putString("last_open", f.getAbsolutePath());
        PokeredEditor.getEditor().getPreferences().putString("last_cwd", f.getParent());
        PokeredEditor.getEditor().getPreferences().save();
        this.setTileMap(t);
    }

    public void addMapChangeListener(MapChangeListener listener) {
        this.mapChangeListeners.add(listener);
    }

    public void removeMapChangeListener(MapChangeListener listener) {
        this.mapChangeListeners.remove(listener);
    }

}
