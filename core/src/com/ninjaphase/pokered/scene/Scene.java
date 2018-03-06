package com.ninjaphase.pokered.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.ninjaphase.pokered.PokemonApplication;

/**
 * <p>
 *     The {@code Scene} handles the updating, rendering and disposal of certain sections of the
 *     game.
 * </p>
 */
abstract class Scene implements Disposable {

    PokemonApplication app;

    /**
     * <p>
     *     Constructs a new {@code Scene}.
     * </p>
     *
     * @param app The application.
     */
    Scene(PokemonApplication app) {
        this.app = app;
    }

    /**
     * <p>
     *     Handles the updating of the scene.
     * </p>
     *
     * @param deltaTime The delta time passed.
     */
    abstract void update(float deltaTime);

    /**
     * <p>
     *     Renders the application.
     * </p>
     *
     * @param batch The sprite batch.
     */
    abstract void render(SpriteBatch batch);

    /**
     * Called when a key is pressed.
     *
     * @param keycode The key code.
     * @return Whether the key was used.
     */
    boolean keyDown(int keycode) {
        return false;
    }


    /**
     * Called when a key is released.
     *
     * @param keycode The key code.
     * @return Whether the key was used.
     */
    boolean keyUp(int keycode) {
        return false;
    }

}
