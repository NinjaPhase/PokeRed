package com.ninjaphase.pokered.entity.event;

import com.badlogic.gdx.utils.JsonValue;

/**
 * <p>
 *     The {@code WarpMapEvent} is used to warp the player to other locations.
 * </p>
 */
public class WarpMapEvent extends MapEvent {

    private String mapName;
    private int x, y;

    /**
     * <p>
     *     Constructs a new {@code WarpMapEvent}.
     * </p>
     *
     * @param params The params.
     */
    public WarpMapEvent(JsonValue params) {
       this.mapName = params.getString("map");
       this.x = params.get("position").getInt(0);
       this.y = params.get("position").getInt(1);
    }

    @Override
    public void begin(MapEventPlayer player) {
        player.warp(this.mapName, x, y);
    }

    @Override
    public boolean isFinished(MapEventPlayer player) {
        return true;
    }

}
