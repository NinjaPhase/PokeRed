package com.ninjaphase.pokered.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.ninjaphase.pokered.PokemonApplication;
import com.ninjaphase.pokered.util.ResourceManager;

/**
 * <p>
 *     The {@code Scene} handles the updating, rendering and disposal of certain sections of the
 *     game.
 * </p>
 */
public abstract class Scene implements Disposable {

    protected PokemonApplication app;
    protected ResourceManager resourceManager;

    /**
     * <p>
     *     Constructs a new {@code Scene}.
     * </p>
     *
     * @param app The application.
     */
    protected Scene(PokemonApplication app) {
        this.app = app;
        this.resourceManager = new ResourceManager(this.app.getResourceManager());
    }

    /**
     * <p>
     *     Handles the updating of the scene.
     * </p>
     *
     * @param deltaTime The delta time passed.
     */
    protected abstract void update(float deltaTime);

    /**
     * <p>
     *     Renders the application.
     * </p>
     *
     * @param batch The sprite batch.
     */
    protected abstract void render(SpriteBatch batch);

    /**
     * Called when a key is pressed.
     *
     * @param keycode The key code.
     * @return Whether the key was used.
     */
    protected boolean keyDown(int keycode) {
        return false;
    }


    /**
     * Called when a key is released.
     *
     * @param keycode The key code.
     * @return Whether the key was used.
     */
    protected boolean keyUp(int keycode) {
        return false;
    }

}
