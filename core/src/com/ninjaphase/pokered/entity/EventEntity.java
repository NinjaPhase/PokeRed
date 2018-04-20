package com.ninjaphase.pokered.entity;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.ninjaphase.pokered.data.map.TileMap;
import com.ninjaphase.pokered.entity.event.MapEvent;
import com.ninjaphase.pokered.entity.event.MessageMapEvent;
import com.ninjaphase.pokered.entity.event.SceneMapEvent;
import com.ninjaphase.pokered.entity.event.WarpMapEvent;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *     The {@code EventEntity} handles an entity which will fire off events.
 * </p>
 */
public class EventEntity extends HumanEntity implements Disposable {

    public final JsonValue data;

    private List<MapEvent> eventList;

    /**
     * <p>
     *     Constructs an {@code EventEntity}.
     * </p>
     *
     * @param map The map.
     */
    public EventEntity(TileMap map, JsonValue data) {
        super(null, map, data.get("position").getInt(0), map.getHeight()-data.get("position").getInt(1)-1);
        this.eventList = new LinkedList<>();
        this.data = data;
        this.parseEvents();
    }

    /**
     * <p>
     *     Parses the events and converts them to the objects.
     * </p>
     */
    private void parseEvents() {
        MapEvent prevEvent = null;
        for(int i = 0; i < this.data.get("events").size; i++) {
            String type = this.data.get("events").get(i).getString(0);
            JsonValue params = this.data.get("events").get(i).get(1);
            MapEvent event;
            if(type.equalsIgnoreCase("message")) {
                this.eventList.add(event = new MessageMapEvent(params));
            } else if(type.equalsIgnoreCase("warp")) {
                this.eventList.add(event = new WarpMapEvent(params));
            } else if(type.equalsIgnoreCase("scene")) {
                this.eventList.add(event = new SceneMapEvent(params));
            } else {
                throw new RuntimeException("Invalid event type " + type);
            }
            if(prevEvent != null) {
                prevEvent.setNextEvent(event);
            }
            prevEvent = event;
        }
    }

    @Override
    public void dispose() {
        if(this.animations != null)
            this.animations[0].getKeyFrame(0.0f).getTexture().dispose();
    }

    /**
     * @return The first event to fire off.
     */
    public MapEvent getFirstEvent() {
        return this.eventList.get(0);
    }

    /**
     * @return Gets the trigger.
     */
    public String getTrigger() {
        return this.data.getString("trigger");
    }

}
