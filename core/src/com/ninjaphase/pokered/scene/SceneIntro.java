package com.ninjaphase.pokered.scene;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ninjaphase.pokered.PokemonApplication;
import com.ninjaphase.pokered.scene.gui.MessageBox;
import com.ninjaphase.pokered.util.Constants;

/**
 * <p>
 *     The {@code SceneIntro} is used to display an introduction to the player.
 * </p>
 */
class SceneIntro extends Scene {
    private static final String[] messages = new String[]{
            "Hello there!\nGlad to meet you!",
            "Welcome to the world of POKéMON!",
            "My name is REDWOOD.\nI am your commander.",
            "You cadet, have been selected to travel through multiple\nworlds of POKéMON!",
            "Now tell me. Are you a boy?\nOr are you a girl?",
            "Let's begin with your name.\nWhat is it?",
            "Right...\nSo your name is PLAYER.",
            "PLAYER!\nYou are about to embark on a very special journey.",
            "Exploring the known Galaxy.\nLooking for rare and wonderous POKéMON.",
            "A world of excitement awaits you.\nLet's go!"
    };

    private int message;
    private MessageBox messageBox;

    /**
     * <p>
     *     Constructs a new {@code SceneIntro}.
     * </p>
     *
     * @param app The application.
     */
    SceneIntro(PokemonApplication app) {
        super(app);
        BitmapFont font = app.getResourceManager().get(BitmapFont.class, Constants.DEFAULT_FONT);
        NinePatch patch = new NinePatch(
                app.getResourceManager().get(Texture.class, Constants.DEFAULT_WINDOWSKIN),
                16, 16, 16, 16);
        this.messageBox = new MessageBox(patch, font);
        this.messageBox.setMessage(messages[this.message++]);
        this.messageBox.setVisible(true);
    }

    @Override
    protected void update(float deltaTime) {
        this.messageBox.update(deltaTime);
    }

    @Override
    protected void render(SpriteBatch batch) {
        this.messageBox.render(batch);
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.Z) {
            if(this.messageBox.isFinished() && message < messages.length) {
                this.messageBox.setMessage(messages[this.message++]);
            } else {
                app.getSceneManager().push(new SceneMap(app));
            }
            return true;
        }
        return false;
    }
}
