package com.ninjaphase.pokered.entity;

import com.ninjaphase.pokered.data.map.TileData;

/**
 * <p>
 *     The {@code EntityDirection} handles the directions for each entity.
 * </p>
 */
public enum EntityDirection {
    DOWN(TileData.TILE_SOLID_DOWN),
    LEFT(TileData.TILE_SOLID_LEFT),
    RIGHT(TileData.TILE_SOLID_RIGHT),
    UP(TileData.TILE_SOLID_UP);

    private int solidFlag;

    /**
     * <p>
     *     Constructs a new {@code EntityDirection}.
     * </p>
     *
     * @param solidFlag The solid flag for this direction
     */
    EntityDirection(int solidFlag) {
        this.solidFlag = solidFlag;
    }

    /**
     * @return The flag used for solid tiles.
     */
    public int getSolidFlag() {
        return this.solidFlag;
    }

    public EntityDirection getOpposite() {
        return EntityDirection.getOpposite(this);
    }

    public static EntityDirection getOpposite(EntityDirection dir) {
        switch(dir) {
            case DOWN:
                return EntityDirection.UP;
            case UP:
                return EntityDirection.DOWN;
            case RIGHT:
                return EntityDirection.LEFT;
            case LEFT:
                return EntityDirection.RIGHT;
        }
        return null;
    }
}
