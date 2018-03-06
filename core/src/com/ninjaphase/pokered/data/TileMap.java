package com.ninjaphase.pokered.data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.JsonValue;
import com.ninjaphase.pokered.entity.Entity;
import com.ninjaphase.pokered.entity.EventEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     The {@code TileMap} handles the map data for the game.
 * </p>
 */
public class TileMap {

    private int width, height;
    private String name, internalName;
    private int[] tiles;
    private boolean[] collisions;
    private Texture texTiles;
    private List<Entity> entities;

    /**
     * <p>
     *     Constructs a new {@code TileMap}.
     * </p>
     *
     * @param texTiles The texture tiles.
     * @param data The map data.
     */
    TileMap(Texture texTiles, JsonValue data) {
        this.entities = new ArrayList<>();
        this.texTiles = texTiles;
        this.name = data.getString("name");
        this.internalName = data.getString("internal_name");
        this.width = data.getInt("width");
        this.height = data.getInt("height");
        this.tiles = new int[this.width*this.height];
        this.collisions = new boolean[this.width*this.height];
        for(int i = 0; i < this.width*this.height; i++) {
            this.tiles[i] = data.get("tiles").getInt(i);
            this.collisions[i] = data.get("collisions").getInt(i) == 1;
        }
        for(int i = 0; i < data.get("entities").size; i++) {
            this.entities.add(new EventEntity(this, data.get("entities").get(i)));
        }
    }

    /**
     * <p>
     *     Renders the {@code TileMap}.
     * </p>
     *
     * @param batch The batch to render onto.
     */
    public void render(SpriteBatch batch) {
        for(int y = 0; y < this.height; y++) {
            for(int x = 0; x < this.width; x++) {
                int tX = this.tiles[x+(y*this.width)]%8;
                int tY = this.tiles[x+(y*this.width)]/8;
                batch.draw(texTiles, x*16.0f, (this.height-y-1)*16.0f, tX*16, tY*16,16, 16);
            }
        }
    }

    /**
     * <p>
     *     Gets the entity at a location.
     * </p>
     *
     * @param x The x position.
     * @param y The y position.
     * @return The entity at a given location.
     */
    public Entity getEntityAt(int x, int y) {
        for(Entity e : this.entities) {
            if(e.getX() != x || e.getY() != y)
                continue;
            return e;
        }
        return null;
    }

    /**
     * <p>
     *     Gets the entity of a certain type at a location.
     * </p>
     *
     * @param x The x position.
     * @param y The y position.
     * @return The entity at a given location.
     */
    public <T extends Entity> T getEntityAt(Class<T> clz, int x, int y) {
        for(Entity e : this.entities) {
            if(e.getClass() != clz || e.getX() != x || e.getY() != y)
                continue;
            return (T) e;
        }
        return null;
    }

    /**
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The collision at the x and y.
     */
    public boolean getCollision(int x, int y) {
        return this.collisions[x+((this.height-y-1)*this.width)];
    }

    /**
     * @return The internal name.
     */
    String getInternalName() {
        return this.internalName;
    }

    /**
     * @return The width of the map.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * @return The height of the map.
     */
    public int getHeight() {
        return this.height;
    }

}
