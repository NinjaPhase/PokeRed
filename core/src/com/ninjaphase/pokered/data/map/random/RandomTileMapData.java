package com.ninjaphase.pokered.data.map.random;

import com.badlogic.gdx.utils.JsonValue;
import com.ninjaphase.pokered.data.map.TileSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     The {@code RandomTileMapData} contains data which will create a
 *     {@link com.ninjaphase.pokered.data.map.TileMap TileMap}.
 * </p>
 * <p>
 *     This is not a fully fledged map but a template for maps to follow when being built.
 * </p>
 */
public class RandomTileMapData {

    public final int width, height, startX, startY;
    public final int[] tiles;
    public final String[] tags;
    public final TileSet tileSet;
    public final Map<String, Integer> connectionOffsets;
    private boolean hasEncounter;

    /**
     * <p>
     *     The {@code RandomTileMapData} holds the data for a random tile map.
     * </p>
     *
     * @param data The data.
     */
    public RandomTileMapData(TileSet tileSet, JsonValue data) {
        this.connectionOffsets = new HashMap<>();
        this.hasEncounter = false;
        this.tileSet = tileSet;
        this.width = data.getInt("width");
        this.height = data.getInt("height");
        this.startX = data.getInt("start_x", 0);
        this.startY = data.getInt("start_y", 0);
        this.tiles = new int[width*height];
        for(int i = 0; i < data.get("tiles").size; i++) {
            this.tiles[i] = data.get("tiles").getInt(i) + data.getInt("tile_offset", 0);
            if(tileSet.isTileEncounter(this.tiles[i]))
                this.hasEncounter = true;
        }
        this.tags = data.get("tags").asStringArray();
        connectionOffsets.put("north", 0);
        connectionOffsets.put("east", 0);
        connectionOffsets.put("south", 0);
        connectionOffsets.put("west", 0);
        if(data.has("connection")) {
            JsonValue conJson = data.get("connection");
            connectionOffsets.put("north", conJson.getInt("north", 0));
            connectionOffsets.put("east", conJson.getInt("east", 0));
            connectionOffsets.put("south", conJson.getInt("south", 0));
            connectionOffsets.put("west", conJson.getInt("west", 0));
            System.out.println(connectionOffsets);

        }
        Arrays.sort(this.tags);
    }

    /**
     * @return Whether the data has an encounter.
     */
    public boolean hasEncounter() {
        return this.hasEncounter;
    }

}
