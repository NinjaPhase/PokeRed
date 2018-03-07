package com.ninjaphase.pokered.editor.data.entity.event;

import com.ninjaphase.pokered.editor.data.entity.event.node.EventNode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * <p>
 *     A {@code EntityEvent} is an event which the entity can use, such as talk or warp.
 * </p>
 */
public abstract class EntityEvent {

    String name;
    JSONObject params;

    private EventNode node;

    /**
     * <p>
     *     Constructs a new {@code EntityEvent}.
     * </p>
     *
     * @param name The name of the entity event.
     */
    EntityEvent(String name) {
        this.name = name;
        this.params = new JSONObject();
        this.node = new EventNode(this);
    }

    public abstract String toNodeString();

    public String toJSONString() {
        return "[\"" + this.name + "\", " + this.params.toString() + "]";
    }

    /**
     * <p>
     *     Parses an event.
     * </p>
     *
     * @param ev The event.
     * @return The event.
     */
    public static EntityEvent parseEvent(JSONArray ev) {
        try {
            switch (ev.getString(0)) {
                case "warp":
                    return new WarpEvent(ev.getJSONObject(1));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return The event node for this event.
     */
    public EventNode getNode() {
        return this.node;
    }

}
