package com.ninjaphase.pokered.entity.event;

import com.ninjaphase.pokered.scene.gui.MessageBox;

/**
 * <p>
 *     The {@code MapEventPlayer} handles events on the map.
 * </p>
 */
public interface MapEventPlayer {

    /**
     * <p>
     *     Queues an event.
     * </p>
     *
     * @param event The event.
     */
    void queueEvent(MapEvent event);

    /**
     * <p>
     *     Warps to an internal name.
     * </p>
     *
     * @param internalName The internal name.
     * @param x The x position.
     * @param y The y position.
     */
    void warp(String internalName, int x, int y);

    /**
     * @return The message box for the map.
     */
    MessageBox getMessageBox();

}
