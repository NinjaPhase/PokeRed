package com.ninjaphase.pokered.editor.components;

import com.ninjaphase.pokered.editor.data.story.tilemap.TileSet;

/**
 * <p>
 *     The {@code TileSelection} class is a simple class the holds a tile selection for the {@code TileSetViewer}.
 * </p>
 *
 * @author NinjaPhase
 */
class TileSelection {

    int w = 1, h = 1;
    int[] tiles = new int[]{0};

    /**
     * <p>
     *     Gets the tiles in a given selection and fills the tiles array.
     * </p>
     *
     * @param tileSet The tileset.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param w The w coordinate.
     * @param h The h coordinate.
     */
    void getTilesInSelection(TileSet tileSet, int x, int y, int w, int h) {
        if(w*h != this.w*this.h)
            this.tiles = new int[w*h];
        this.w = w;
        this.h = h;
        for(int yy = 0; yy < h; yy++) {
            for(int xx = 0; xx < w; xx++) {
                this.tiles[xx+(yy*w)] = (x+xx)+((y+yy)*tileSet.getImageColumns());
            }
        }
    }

    int getTile(int x, int y) {
        return this.tiles[(x%this.w)+((y%this.h)*this.w)];
    }

}
