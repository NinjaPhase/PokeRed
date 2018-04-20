package com.ninjaphase.pokered.data.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import com.ninjaphase.pokered.entity.EntityDirection;
import com.ninjaphase.pokered.util.ResourceManager;

/**
 * <p>
 *     The {@code TileSet} handles the tileset for a {@link TileMap}.
 * </p>
 * <p>
 *     It also contains the data for each tile to decide whether they are solid or not.
 * </p>
 */
public class TileSet {
    private int tWidth, tHeight;
    private int[] tileData;

    private TextureRegion[] tiles;

    /**
     * <p>
     *     Constructs a new {@code TileSet}.
     * </p>
     *
     * @param rm The {@code ResourceManager} to get the texture from.
     * @param v The JsonValue to initialise the tileset from.
     */
    public TileSet(ResourceManager rm, JsonValue v) {
        this.tWidth = v.getInt("width");
        this.tHeight = v.getInt("height");
        
        Texture texture = rm.get(Texture.class, v.getString("texture"));
        int cols = texture.getWidth()/this.tWidth;
        int rows = texture.getHeight()/this.tHeight;
        this.tiles = new TextureRegion[cols*rows];
        this.tileData = new int[cols*rows];
        JsonValue tileDataJson = null;
        if(v.has("tile_data"))
            tileDataJson = v.get("tile_data");
        for(int y = 0; y < rows; y++) {
            for(int x = 0; x < cols; x++) {
                this.tiles[x+(y*cols)] = new TextureRegion(texture,
                        x*this.tWidth, y*this.tHeight, this.tWidth, this.tHeight);
                if(tileDataJson != null) {
                    this.tileData[x + (y * cols)] = tileDataJson.getInt(
                            String.valueOf(x + (y * cols)), 0);
                }
            }
        }
    }

    /**
     * @param i The index.
     * @param flag The flag.
     * @return Whether the tile flag is set.
     */
    private boolean isTileFlagSet(int i, int flag) {
        return (this.tileData[i] & flag) > 0;
    }

    /**
     * @param i The tile index.
     * @return The tile at the specific index.
     */
    public TextureRegion getTile(int i) {
        return this.tiles[i];
    }

    /**
     * @param i The index.
     * @return Whether the tile at the specific index is solid.
     */
    public boolean isTileSolid(EntityDirection dir, int i) {
        return this.isTileFlagSet(i, dir.getSolidFlag());
    }

    /**
     * @param i The index.
     * @return Whether the tile at the specific index is an encounter tile.
     */
    public boolean isTileEncounter(int i) {
        return this.isTileFlagSet(i, TileData.TILE_ENCOUNTER);
    }

}
