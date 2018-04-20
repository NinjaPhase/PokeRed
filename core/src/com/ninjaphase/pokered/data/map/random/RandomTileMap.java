package com.ninjaphase.pokered.data.map.random;

import com.ninjaphase.pokered.PokemonApplication;
import com.ninjaphase.pokered.data.map.EncounterData;
import com.ninjaphase.pokered.data.map.TileMap;
import com.ninjaphase.pokered.entity.EntityDirection;

import java.util.Arrays;

/**
 * <p>
 *     The {@code RandomTileMap} is a tile map made from random data.
 * </p>
 */
class RandomTileMap extends TileMap {

    public final RandomTileMapData data;

    /**
     * <p>
     *     Constructs a new {@code RandomTileMap}.
     * </p>
     *
     * @param data The tile map data.
     */
    RandomTileMap(String name, RandomTileMapData data, int wildLevel) {
        super();
        this.data = data;
        this.name = name;
        this.tileSet = data.tileSet;
        this.width = data.width;
        this.height = data.height;
        this.tiles = Arrays.copyOf(data.tiles, data.tiles.length);
        if(data.hasEncounter()) {
            int minLevel = wildLevel-2;
            int maxLevel = wildLevel+1;
            this.encounterData = new EncounterData();
            EncounterData edCommon = PokemonApplication.getApplication().getDataManager().randomSpeciesEncounters.get("common_grass");
            EncounterData edRare = PokemonApplication.getApplication().getDataManager().randomSpeciesEncounters.get("rare_grass");
            this.encounterData.addEncounter(edCommon.getEncounter().species.getIndex()-1, Math.max(2, (int)(Math.random()*(maxLevel-minLevel)+minLevel)), (int)(Math.random()*200f)+50);
            this.encounterData.addEncounter(edCommon.getEncounter().species.getIndex()-1, Math.max(2, (int)(Math.random()*(maxLevel-minLevel)+minLevel)), (int)(Math.random()*200f)+50);
            this.encounterData.addEncounter(edCommon.getEncounter().species.getIndex()-1, Math.max(2, (int)(Math.random()*(maxLevel-minLevel)+minLevel)), (int)(Math.random()*200f)+50);
            this.encounterData.addEncounter(edRare.getEncounter().species.getIndex()-1, Math.max(2, (int)(Math.random()*(maxLevel-minLevel)+minLevel)), (int)(Math.random()*50f)+20);
            this.encounterData.addEncounter(edRare.getEncounter().species.getIndex()-1, Math.max(2, (int)(Math.random()*(maxLevel-minLevel)+minLevel)), (int)(Math.random()*50f)+20);
            this.encounterData.normaliseWeights();
        }
    }

    /**
     * <p>
     *     Adds a connection to the tile map.
     * </p>
     *
     * @param dir The direction.
     * @param tm The tile map.
     */
    void addConnection(EntityDirection dir, RandomTileMap tm, int offset) {
        if(this.hasConnection(dir))
            return;
        TileMapConnection connection = new TileMapConnection();
        connection.map = tm;
        connection.offset = offset;
        this.connections.put(dir, connection);
        tm.addConnection(dir.getOpposite(), this, offset < 0 ? Math.abs(offset) : -offset);
    }

}
