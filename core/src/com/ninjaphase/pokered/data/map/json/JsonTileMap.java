package com.ninjaphase.pokered.data.map.json;

import com.badlogic.gdx.utils.JsonValue;
import com.ninjaphase.pokered.data.map.EncounterData;
import com.ninjaphase.pokered.data.map.TileMap;
import com.ninjaphase.pokered.data.map.TileSet;
import com.ninjaphase.pokered.data.story.Story;
import com.ninjaphase.pokered.entity.EntityDirection;
import com.ninjaphase.pokered.entity.EventEntity;

/**
 * <p>
 *     A {@code JsonTileMap} is a tile map which uses loaded json.
 * </p>
 */
public class JsonTileMap extends TileMap {

    /**
     * <p>
     *     Constructs a new {@code JsonTileMap} from given json data.
     * </p>
     */
    public JsonTileMap(TileSet tileSet, JsonValue data) {
        super();
        this.tileSet = tileSet;
        this.name = data.getString("name");
        this.internalName = data.getString("internal_name");
        this.width = data.getInt("width");
        this.height = data.getInt("height");
        this.tiles = new int[this.width*this.height];
        for(int i = 0; i < data.get("tiles").size; i++) {
            this.tiles[i] = data.get("tiles").getInt(i);
        }
        if(data.has("connections")) {
            for(JsonValue jv : data.get("connections")) {
                TileMapConnection tmc = new TileMapConnection();
                tmc.mapName = jv.getString(0);
                if(jv.size == 2)
                    tmc.offset = jv.getInt(1);
                switch(jv.name) {
                    case "south":
                        this.connections.put(EntityDirection.DOWN, tmc);
                        break;
                    case "north":
                        this.connections.put(EntityDirection.UP, tmc);
                        break;
                    case "east":
                        this.connections.put(EntityDirection.RIGHT, tmc);
                        break;
                    case "west":
                        this.connections.put(EntityDirection.LEFT, tmc);
                        break;
                }
            }
        }
        if(data.has("entities")) {
            for(JsonValue jv : data.get("entities")) {
                this.entities.add(new EventEntity(this, jv));
            }
        }
        if(data.has("encounters")) {
            this.encounterData = new EncounterData(data.get("encounters"));
        }
    }

    /**
     * <p>
     *     Links the connections.
     * </p>
     *
     * @param story The story.
     */
    public void linkConnections(Story story) {
        for(EntityDirection dir : this.connections.keySet()) {
            this.connections.get(dir).map = story.getMap(this.connections.get(dir).mapName);
        }
    }

}
