package com.ninjaphase.pokered.editor.data.story.tilemap;

import java.util.Arrays;

/**
 * <p>
 *     The {@code TileSetApplier} is used as an interface between the tileset to apply or revert new data.
 * </p>
 */
public class TileSetApplier extends TileSet {

    private TileSet ts;

    /**
     * <p>
     *     Constructs a new {@code TileSetApplier}.
     * </p>
     *
     * @param ts The tileset.
     */
    public TileSetApplier(TileSet ts) {
        super(ts.story, ts.getId());
        this.ts = ts;
        this.name = ts.name;
        this.tilesetImage = ts.tilesetImage;
        this.tileData = Arrays.copyOf(ts.tileData, ts.tileData.length);
    }

    /**
     * <p>
     *     Applies the data to the underlying tileset.
     * </p>
     */
    public void apply() {
        this.ts.setName(this.name);
        this.ts.setTilesetImage(this.tilesetImage);
        this.ts.tileData = Arrays.copyOf(this.tileData, this.tileData.length);
    }

}
