package com.ninjaphase.pokered.entity.event;

import com.badlogic.gdx.utils.JsonValue;

/**
 * <p>
 *     The {@code MessageMapEvent} is used to display a message on the map.
 * </p>
 */
public class MessageMapEvent extends MapEvent {

    private String message;

    /**
     * <p>
     *     Constructs a new {@code MessageMapEvent}.
     * </p>
     *
     * @param params The message parameters.
     */
    public MessageMapEvent(JsonValue params) {
        this.message = params.getString("text");
    }

    @Override
    public void begin(MapEventPlayer player) {
        player.getMessageBox().setMessage(this.message);
        player.getMessageBox().setVisible(true);
    }

    @Override
    public boolean isFinished(MapEventPlayer player) {
        return !player.getMessageBox().isVisible();
    }

}
