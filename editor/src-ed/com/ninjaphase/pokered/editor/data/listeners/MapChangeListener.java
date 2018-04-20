package com.ninjaphase.pokered.editor.data.listeners;

import com.ninjaphase.pokered.editor.data.story.tilemap.TileMap;

/**
 * <p>
 *     The {@code MapChangeListener} listens for map change events.
 * </p>
 */
public interface MapChangeListener {

    /**
     * <p>
     *     Called when the map is changed.
     * </p>
     * @param newMap The new map.
     */
    void onMapChange(TileMap newMap);

}
