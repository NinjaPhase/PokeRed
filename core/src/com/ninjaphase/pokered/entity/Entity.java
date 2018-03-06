package com.ninjaphase.pokered.entity;

import com.badlogic.gdx.math.Vector2;
import com.ninjaphase.pokered.data.TileMap;

/**
 * <p>
 *     The {@code Entity} is an object which does not move but has a world position.
 * </p>
 */
public abstract class Entity {

    public final Vector2 position;

    int tileX, tileY;
    TileMap map;

    /**
     * <p>
     *     Constructs a new {@code TileMap}.
     * </p>
     *
     * @param map The tilemap.
     */
    public Entity(TileMap map) {
        this(map, 0, 0);
    }

    /**
     * <p>
     *     Constructs a new {@code TileMap}.
     * </p>
     *
     * @param map The map.
     * @param x The x position.
     * @param y The y position.
     */
    public Entity(TileMap map, int x, int y) {
        this.position = new Vector2(x * 16, y * 16);
        this.map = map;
        this.tileX = x;
        this.tileY = y;
    }

    /**
     * <p>
     *     Sets the map the entity is on.
     * </p>
     *
     * @param map The map.
     */
    public void setMap(TileMap map, int x, int y) {
        this.map = map;
        this.position.set(x*16,y*16);
        this.tileX = x;
        this.tileY = y;
    }

    /**
     * @return The tiles x position.
     */
    public int getX() {
        return this.tileX;
    }

    /**
     * @return The tiles y position.
     */
    public int getY() {
        return this.tileY;
    }

    /**
     * @return The map the entity belongs to.
     */
    public TileMap getMap() {
        return this.map;
    }

}
