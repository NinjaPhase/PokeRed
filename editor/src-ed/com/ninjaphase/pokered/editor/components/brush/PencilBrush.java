package com.ninjaphase.pokered.editor.components.brush;

import com.ninjaphase.pokered.editor.data.story.tilemap.TileMap;

/**
 * <p>
 *     The {@code PencilBrush} is the simplest brush, and is used to paint a tile or a set of tiles onto the
 *     map.
 * </p>
 */
public class PencilBrush implements Brush {

    @Override
    public void onBrushBegin(TileMap map, int tileId, int x, int y) {
        // map.setTileAt(tileId, x, y);
    }

    @Override
    public void onBrushMove(TileMap map, int tileId, int x, int y) {
        // map.setTileAt(tileId, x, y);
    }

    @Override
    public void onBrushEnd(TileMap map, int tileId, int x, int y) {
        // map.setTileAt(tileId, x, y);
    }
}
