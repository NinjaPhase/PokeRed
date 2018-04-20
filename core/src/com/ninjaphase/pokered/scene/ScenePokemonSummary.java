package com.ninjaphase.pokered.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ninjaphase.pokered.PokemonApplication;
import com.ninjaphase.pokered.data.pokemon.Pokemon;

/**
 * <p>
 *     The {@code ScenePokemonSummary} is used to display pokemon data to the player.
 * </p>
 */
class ScenePokemonSummary extends Scene {

    enum WindowType {
        POKEMON_INFORMATION
    }

    private Pokemon pkmn;
    private TextureAtlas atlasSummary, atlasTypes;
    private TextureRegion texBackground, texInformationBar, texInformationBox, texRightBox;
    private BitmapFont fntSummary;
    private GlyphLayout glSpecies;

    private TextureRegion typeRegionOne;

    /**
     * <p>
     *     Constructs a new {@code ScenePokemonSummary} screen.
     * </p>
     *
     * @param app The application.
     * @param pkmn The pokemon.
     */
    ScenePokemonSummary(PokemonApplication app, Pokemon pkmn) {
        super(app);
        this.pkmn = pkmn;
        this.atlasTypes = new TextureAtlas("img/system/types.pack");
        this.atlasSummary = new TextureAtlas("img/system/summary_screen.pack");
        this.texBackground = this.atlasSummary.findRegion("summary_bg");
        this.texInformationBar = this.atlasSummary.findRegion("information_bar");
        this.texInformationBox = this.atlasSummary.findRegion("information_box");
        this.texRightBox = this.atlasSummary.findRegion("right_box");
        this.fntSummary = new BitmapFont(Gdx.files.internal("font/black_font.fnt"));
        this.glSpecies = new GlyphLayout(fntSummary, this.pkmn.getSpecies().getName());
        this.typeRegionOne = this.atlasTypes.findRegion(this.pkmn.getSpecies().getTypes()[0].toString().toLowerCase());
    }

    @Override
    protected void update(float deltaTime) {

    }

    @Override
    protected void render(SpriteBatch batch) {
        batch.draw(this.texBackground, 0.0f, 0.0f);
        batch.draw(this.texInformationBar, 0.0f, app.getDefaultCamera().viewportHeight - this.texInformationBar.getRegionHeight() - 4.0f);
        batch.draw(this.texInformationBox, 0.0f, 0.0f);
        this.fntSummary.draw(batch, this.glSpecies, 151.0f - (glSpecies.width / 2f), 129.0f);
        batch.draw(this.texRightBox,
                app.getDefaultCamera().viewportWidth - this.texRightBox.getRegionWidth(),
                app.getDefaultCamera().viewportHeight - this.texRightBox.getRegionHeight() - 4.0f);
        if(this.pkmn.getSpecies().getTypes().length == 2) {

        } else {
            batch.draw(this.typeRegionOne, 126.0f, 105.0f);
        }
        this.fntSummary.draw(batch, "No. " + this.pkmn.getSpecies().getIndex(), 243f, 170f);
        this.fntSummary.draw(batch, this.pkmn.getName(), 243f, 54f);
        this.fntSummary.draw(batch, "Lv. " + this.pkmn.getLevel(), 252f, 39f);
        batch.draw(this.pkmn.getSpecies().getFrontTexture(), 241.0f, 80.0f);
    }

    @Override
    public void dispose() {
        this.atlasSummary.dispose();
        this.fntSummary.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.X) {
            this.app.getSceneManager().pop();
            return true;
        }
        return false;
    }
}
