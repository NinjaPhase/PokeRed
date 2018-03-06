package com.ninjaphase.pokered.editor.data;

import com.ninjaphase.pokered.editor.data.listeners.MapChangeListener;

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

    public void addMapChangeListener(MapChangeListener listener) {
        this.mapChangeListeners.add(listener);
    }

    public void removeMapChangeListener(MapChangeListener listener) {
        this.mapChangeListeners.remove(listener);
    }

}
