package com.ninjaphase.pokered.editor.data.entity.event;

import com.ninjaphase.pokered.editor.data.entity.event.node.EventNode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * <p>
 *     A {@code WarpEvent} is an event which handles warping from one location to another.
 * </p>
 */
public class WarpEvent extends EntityEvent {

    private String internalName;
    private int x, y;


    /**
     * <p>
     *     The {@code WarpEvent}.
     * </p>
     */
    public WarpEvent() {
        super("warp");
        this.setMap("this");
        this.setPosition(0, 0);
    }

    /**
     * <p>
     *     Constructs a new {@code WarpEvent} from existing data.
     * </p>
     *
     * @param params The params.
     */
    public WarpEvent(JSONObject params) {
        this();
        this.setMap(params.optString("map"));
        this.setPosition(params.optJSONArray("position").optInt(0), params.optJSONArray("position").optInt(1));
    }

    /**
     * <p>
     *     Sets the map.
     * </p>
     *
     * @param str The maps internal name.
     */
    public void setMap(String str) {
        try {
            this.params.remove("map");
            this.params.put("map", str);
            this.internalName = str;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     *     Sets the position of the warp.
     * </p>
     *
     * @param x The x position.
     * @param y The y position.
     */
    public void setPosition(int x, int y) {
        try {
            JSONArray newArray = new JSONArray();
            newArray.put(x);
            newArray.put(y);
            this.params.remove("position");
            this.params.put("position", newArray);
            this.x = x;
            this.y = y;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toNodeString() {
        return "Warp[" + this.internalName + "], (" + this.x + "," + this.y + ")";
    }
}
