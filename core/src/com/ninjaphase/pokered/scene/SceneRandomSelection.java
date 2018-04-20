package com.ninjaphase.pokered.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ninjaphase.pokered.PokemonApplication;
import com.ninjaphase.pokered.data.map.random.RandomRegion;
import com.ninjaphase.pokered.entity.Player;
import com.ninjaphase.pokered.scene.gui.ChoiceBox;
import com.ninjaphase.pokered.util.Constants;

/**
 * <p>
 *     The {@code SceneRandomSelection} is used to show the randomly generated maps the player has
 *     generated and to enter them.
 * </p>
 */
public class SceneRandomSelection extends Scene {

    private Player player;
    private int selected;

    private TextureAtlas atlasRandom;
    private TextureRegion texBackground, texButton, texButtonHover, texMapInfo;

    private BitmapFont font;

    private RandomRegion[] randomRegions;

    /**
     * <p>
     *     Constructs a new {@code SceneRandomSelection}.
     * </p>
     *
     * @param app The application.
     */
    public SceneRandomSelection(PokemonApplication app) {
        super(app);
        this.player = app.getDataManager().getCurrentStory().getPlayer();
        this.randomRegions = this.player.randomRegions;
        this.atlasRandom = new TextureAtlas(Gdx.files.internal("img/system/random_screen.pack"));
        this.texBackground = this.atlasRandom.findRegion("random_bg");
        this.texButton = this.atlasRandom.findRegion("button");
        this.texButtonHover = this.atlasRandom.findRegion("button_hover");
        this.texMapInfo = this.atlasRandom.findRegion("map_info");
        this.font = app.getResourceManager().get(BitmapFont.class, Constants.DEFAULT_FONT);
    }

    @Override
    protected void update(float deltaTime) {
    }

    @Override
    protected void render(SpriteBatch batch) {
        batch.draw(this.texBackground, 0.0f, 0.0f);
        for(int i = 0; i < 3; i++) {
            if(i == selected) {
                batch.draw(this.texButtonHover,
                        8.0f,
                        app.getDefaultCamera().viewportHeight - 8.0f - this.texButton.getRegionHeight() - (21f*i));
            } else {
                batch.draw(this.texButton,
                        8.0f,
                        app.getDefaultCamera().viewportHeight - 8.0f - this.texButton.getRegionHeight() - (21f*i));
            }
            if(this.randomRegions[i] == null) {
                this.font.draw(batch, "Empty Slot", 16f, app.getDefaultCamera().viewportHeight - 15.0f - (21f * i));
            } else {
                this.font.draw(batch, this.randomRegions[i].getName(),
                        16f, app.getDefaultCamera().viewportHeight - 15.0f - (21f * i));
            }
        }
        batch.draw(this.texMapInfo,
                app.getDefaultCamera().viewportWidth - this.texMapInfo.getRegionWidth() - 8.0f,
                8.0f);
    }

    @Override
    public void dispose() {
        this.atlasRandom.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.Z) {
            RandomRegion region;
            if((region = this.randomRegions[this.selected]) == null) {
                region = new RandomRegion(app.getDataManager().randomTileMapDatas);
                region.generate(20, 20, 6);
                this.randomRegions[this.selected] = region;
            }
            this.player.setMap(region.getStartMap(), region.getStartX(), region.getStartY());
            app.getSceneManager().pop();
            return true;
        } else if(keycode == Input.Keys.DOWN) {
            this.selected = Math.min(2, this.selected + 1);
            return true;
        } else if(keycode == Input.Keys.UP) {
            this.selected = Math.max(0, this.selected - 1);
            return true;
        }
        return false;
    }
}
