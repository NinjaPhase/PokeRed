package com.ninjaphase.pokered.scene;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.ninjaphase.pokered.PokemonApplication;
import com.ninjaphase.pokered.data.story.Story;
import com.ninjaphase.pokered.entity.Player;
import com.ninjaphase.pokered.scene.gui.ChoiceBox;
import com.ninjaphase.pokered.util.Constants;

/**
 * <p>
 *     The {@code SceneTitleMenu} is the title menu for the game.
 * </p>
 */
public class SceneTitleMenu extends Scene {

    private boolean saveExists;
    private ChoiceBox<String> choiceBox;
    private ChoiceBox<Story> stories;
    private Camera camera;

    /**
     * <p>
     *     Constructs the {@code SceneTitleMenu}.
     * </p>
     *
     * @param app The application.
     */
    SceneTitleMenu(PokemonApplication app) {
        super(app);
        this.camera = app.getDefaultCamera();
        PokemonApplication.getMidiPlayer().stop(0);
        NinePatch patch = new NinePatch(
                app.getResourceManager().get(Texture.class, Constants.DEFAULT_WINDOWSKIN),
                16, 16, 16, 16);
        BitmapFont font = app.getResourceManager().get(BitmapFont.class, Constants.DEFAULT_FONT);
        TextureAtlas texMisc = app.getResourceManager().get(TextureAtlas.class, Constants.DEFAULT_MISC);
        TextureRegion texCaret = texMisc.findRegion("caret");
        if(this.saveExists) {
            this.choiceBox = new ChoiceBox<>(patch, font, texCaret,
                    "CONTINUE", "NEW GAME", "OPTION");
        } else {
            this.choiceBox = new ChoiceBox<>(patch, font, texCaret,
                    "NEW GAME", "OPTION");
        }
        this.stories = new ChoiceBox<>(patch, font, texCaret, app.getDataManager().getStoriesAsArray());
    }

    @Override
    protected void update(float deltaTime) {

    }

    @Override
    protected void render(SpriteBatch batch) {
        if(this.saveExists) {
            this.choiceBox.render(batch, 0f, this.camera.viewportHeight - 80f, 120f, 64f);
        } else {
            this.choiceBox.render(batch, 0f, this.camera.viewportHeight - 64f, 120f, 48f);
        }
        this.stories.render(batch, 0f, 0f, PokemonApplication.V_WIDTH, 24f);
    }

    @Override
    public void dispose() {
        this.resourceManager.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.UP) {
            this.choiceBox.setCurrentIndex(this.choiceBox.getCurrentIndex() - 1);
            return true;
        } else if(keycode == Input.Keys.DOWN) {
            this.choiceBox.setCurrentIndex(this.choiceBox.getCurrentIndex() + 1);
            return true;
        } else if(keycode == Input.Keys.Z) {
            if(this.choiceBox.getCurrentValue().equalsIgnoreCase("NEW GAME")) {
                Player player = new Player(app.getDataManager().getCurrentStory().getPlayerGraphic(),
                        app.getDataManager().getCurrentStory().getStartMap(),
                        app.getDataManager().getCurrentStory().getInfo().startMapX,
                        app.getDataManager().getCurrentStory().getInfo().startMapY);
                app.getDataManager().getCurrentStory().setPlayer(player);
                this.app.getSceneManager().push(new SceneIntro(this.app));
            } else if(this.choiceBox.getCurrentValue().equalsIgnoreCase("OPTION")) {

            }
            return true;
        } else if(keycode == Input.Keys.X) {
            this.app.getSceneManager().pop();
            return true;
        }
        return false;
    }

}
