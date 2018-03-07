package com.ninjaphase.pokered.editor.data.entity;

import com.ninjaphase.pokered.editor.data.entity.event.EntityEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *     A {@code Entity} holds data for the entity within the game.
 * </p>
 */
public class Entity {

    private int x, y;
    private List<EntityEvent> events;

    /**
     * <p>
     *     The {@code Entity}.
     * </p>
     *
     * @param x The x position.
     * @param y The y position.
     */
    public Entity(int x, int y) {
        this.events = new LinkedList<>();
        this.x = x;
        this.y = y;
    }

    /**
     * <p>
     *     The {@code Entity}
     * </p>
     *
     * @param obj The entity data.
     */
    public Entity(JSONObject obj) {
        this(obj.optJSONArray("position").optInt(0), obj.optJSONArray("position").optInt(1));
        for(int i = 0; i < obj.optJSONArray("events").length(); i++) {
            this.events.add(EntityEvent.parseEvent(obj.optJSONArray("events").optJSONArray(i)));
        }
    }

    /**
     * @return The entity data as a json string.
     */
    public String toJSONString() {
        JSONObject thisObj = new JSONObject();

        try {
            JSONArray position = new JSONArray();
            position.put(this.x);
            position.put(this.y);
            thisObj.put("position", position);
            JSONArray events = new JSONArray();
            for(EntityEvent e : this.events) {
                events.put(new JSONArray(e.toJSONString()));
            }
            thisObj.put("events", events);
            return thisObj.toString(2);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * <p>
     *     Sets the location of the entity.
     * </p>
     *
     * @param x The x position.
     * @param y The y position.
     */
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return The x position.
     */
    public int getX() {
        return this.x;
    }

    /**
     * @return The y position.
     */
    public int getY() {
        return this.y;
    }

    /**
     * @return The events list.
     */
    public List<EntityEvent> getEvents() {
        return this.events;
    }

}
