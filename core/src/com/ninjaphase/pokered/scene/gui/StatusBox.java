package com.ninjaphase.pokered.scene.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * <p>
 *     The {@code StatusBox} is the interface for the status box of the player and enemy.
 * </p>
 */
interface StatusBox {

    /**
     * <p>
     *     Renders the status box.
     * </p>
     *
     * @param batch The batch to render onto.
     * @param x The x location.
     * @param y The y location.
     */
    void render(SpriteBatch batch, float x, float y);

    /**
     * <p>
     *     Sets the new display health value.
     * </p>
     *
     * @param newValue The new health value.
     */
    void setHealth(int newValue);

    /**
     * @return Gets the health value.
     */
    int getHealth();

}
