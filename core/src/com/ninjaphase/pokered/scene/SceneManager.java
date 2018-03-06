package com.ninjaphase.pokered.scene;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import java.util.Stack;

/**
 * <p>
 *     The {@code SceneManager} handles all scenes within the game.
 * </p>
 */
public class SceneManager implements Disposable, InputProcessor {

    private final Stack<Scene> scenes;

    /**
     * <p>
     *     Constructs a new {@code SceneManager}.
     * </p>
     */
    public SceneManager() {
        this.scenes = new Stack<>();
    }

    /**
     * <p>
     *     Handles the updating of the top scene.
     * </p>
     *
     * @param deltaTime The delta time.
     */
    public void update(float deltaTime) {
        synchronized (this.scenes) {
            if(this.scenes.isEmpty())
                return;
            this.scenes.peek().update(deltaTime);
        }
    }

    /**
     * <p>
     *     Handles the rendering of the top scene.
     * </p>
     *
     * @param batch The batch.
     */
    public void render(SpriteBatch batch) {
        synchronized (this.scenes) {
            if(this.scenes.isEmpty())
                return;
            this.scenes.peek().render(batch);
        }
    }

    /**
     * <p>
     *     Pushes a new {@code Scene} to the scene manager.
     * </p>
     *
     * @param scene The scene to push onto the scene manager.
     */
    public void push(Scene scene) {
        synchronized(this.scenes) {
            if (scene == null)
                throw new NullPointerException("Trying to push null Scene to SceneManager.");
            this.scenes.push(scene);
        }
    }

    /**
     * <p>
     *     Pops a {@code Scene} from the stack.
     * </p>
     *
     * @return The scene to pop.
     */
    public Scene pop() {
        synchronized(this.scenes) {
            Scene s = this.scenes.pop();
            s.dispose();
            return s;
        }
    }

    /**
     * <p>
     *     Removes a {@code Scene} from the stack.
     * </p>
     *
     * @param s The scene.
     * @return Whether the scene existed on the stack and was removed.
     */
    public boolean remove(Scene s) {
        synchronized(this.scenes) {
            if (!this.scenes.contains(s))
                return false;
            this.scenes.remove(s);
            s.dispose();
            return true;
        }
    }

    @Override
    public void dispose() {
        synchronized(this.scenes) {
            while(!this.scenes.isEmpty())
                this.scenes.pop().dispose();
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        synchronized(this.scenes) {
            if(this.scenes.isEmpty())
                return false;
            return this.scenes.peek().keyDown(keycode);
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        synchronized(this.scenes) {
            if(this.scenes.isEmpty())
                return false;
            return this.scenes.peek().keyUp(keycode);
        }
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
