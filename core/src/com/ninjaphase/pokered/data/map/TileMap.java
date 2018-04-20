package com.ninjaphase.pokered.data.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ninjaphase.pokered.entity.Entity;
import com.ninjaphase.pokered.entity.EntityDirection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     The {@code TileMap} handles the map data for the game.
 * </p>
 */
public abstract class TileMap {

    protected String name, internalName;
    protected int width, height;
    protected int[] tiles;
    protected TileSet tileSet;

    protected List<Entity> entities;
    protected Map<EntityDirection, TileMapConnection> connections;
    protected EncounterData encounterData;

    /**
     * <p>
     *     Initiliases start data.
     * </p>
     */
    protected TileMap() {
        this.connections = new HashMap<>();
        this.entities = new ArrayList<>();
    }

    /**
     * <p>
     *     Renders the {@code TileMap}.
     * </p>
     *
     * @param batch The batch to render onto.
     */
    public void render(SpriteBatch batch) {
        this.render(batch, 0.0f, 0.0f);
    }

    /**
     * <p>
     *     Renders the {@code TileMap} at x and y.
     * </p>
     *
     * @param batch The batch to render onto.
     */
    public void render(SpriteBatch batch, float vx, float vy) {
        for(int y = 0; y < this.height; y++) {
            for(int x = 0; x < this.width; x++) {
                batch.draw(tileSet.getTile(this.tiles[x+(y*this.width)]),
                        vx+(x*16.0f), vy+((this.height-y-1)*16.0f));
            }
        }
    }

    /**
     * <p>
     *     Renders the {@code TileMap} connections.
     * </p>
     *
     * @param batch The batch to render onto.
     */
    public void renderConnections(SpriteBatch batch) {
        if(this.connections.containsKey(EntityDirection.UP)) {
            TileMapConnection tmc = this.connections.get(EntityDirection.UP);
            tmc.map.render(batch, tmc.offset*16, this.getFullHeight());
        }
        if(this.connections.containsKey(EntityDirection.DOWN)) {
            TileMapConnection tmc = this.connections.get(EntityDirection.DOWN);
            tmc.map.render(batch, tmc.offset*16, -tmc.map.getFullHeight());
        }
        if(this.connections.containsKey(EntityDirection.LEFT)) {
            TileMapConnection tmc = this.connections.get(EntityDirection.LEFT);
            tmc.map.render(batch, -tmc.map.getFullWidth(), tmc.offset*16);
        }
        if(this.connections.containsKey(EntityDirection.RIGHT)) {
            TileMapConnection tmc = this.connections.get(EntityDirection.RIGHT);
            tmc.map.render(batch, this.getFullWidth(), tmc.offset*16);
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
    @SuppressWarnings("unchecked")
    public <T extends Entity> T getEntityAt(Class<T> clz, int x, int y) {
        for(Entity e : this.entities) {
            if(e.getClass() != clz || e.getX() != x || e.getY() != y)
                continue;
            return (T) e;
        }
        return null;
    }

    /**
     * @return The internal name.
     */
    public String getInternalName() {
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

    /**
     * @return The entire full width of the {@code TileMap}.
     */
    public float getFullWidth() {
        return this.getWidth()*16f;
    }

    /**
     * @return The entire full height of the {@code TileMap}.
     */
    public float getFullHeight() {
        return this.getHeight()*16f;
    }

    /**
     * @param dir The direction of the connection.
     * @return {@code true} if there is a connection for that direction, otherwise {@code false}.
     */
    public boolean hasConnection(EntityDirection dir) {
        return this.connections.containsKey(dir);
    }

    /**
     * @param dir The direction of the connection.
     * @return The tilemap at the connection.
     */
    public TileMap getConnection(EntityDirection dir) {
        return this.connections.get(dir).map;
    }

    /**
     * @param dir The direction of the connection.
     * @return The offset of the given connection.
     */
    public int getConnOffset(EntityDirection dir) { return this.connections.get(dir).offset; }

    /**
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The collision at the x and y.
     */
    public boolean getCollision(int x, int y) {
        if(x < 0 || x >= this.width || y < 0 || y >= this.height)
            return true;
        return this.tileSet.isTileSolid(EntityDirection.DOWN,
                this.tiles[x+((this.height-y-1)*this.width)]);
    }

    /**
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return Whether there should be a random encounter at the tile.
     */
    public boolean isEncounterTile(int x, int y) {
        if(x < 0 || x >= this.width || y < 0 || y >= this.height)
            return false;
        return this.tileSet.isTileEncounter(this.tiles[x+((this.height-y-1)*this.width)]);
    }

    /**
     * @return The wild encounter tile.
     */
    public EncounterData getEncounterData() {
        return this.encounterData;
    }

    /**
     * @param x The x tile.
     * @param y The y tile.
     * @return The tile at x and y.
     */
    public int getTileAt(int x, int y) {
        if(x < 0 || x >= this.width || y < 0 || y >= this.height)
            return -1;
        return this.tiles[x + (y * width)];
    }

    /**
     * <p>
     *     The {@code TileMapConnection} holds the connection data for the {@code TileMap}.
     * </p>
     */
    public static class TileMapConnection {
        public TileMap map;
        public String mapName;
        public int offset;
    }

}
