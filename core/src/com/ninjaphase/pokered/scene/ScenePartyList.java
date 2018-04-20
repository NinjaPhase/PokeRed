package com.ninjaphase.pokered.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.ninjaphase.pokered.PokemonApplication;
import com.ninjaphase.pokered.data.pokemon.Pokemon;
import com.ninjaphase.pokered.data.pokemon.Stat;
import com.ninjaphase.pokered.entity.Player;
import com.ninjaphase.pokered.scene.gui.ChoiceBox;
import com.ninjaphase.pokered.util.Constants;

/**
 * <p>
 *     The {@code ScenePartyList} is used to look at the Pokemon currently in the party, select new pokemon and switch
 *     the order of Pokemon.
 * </p>
 */
class ScenePartyList extends Scene {

    private Player player;
    private BitmapFont font;
    private TextureRegion texCaret, texCaretSelected, texHealthbar, texHealthFill, texLevelIcon;
    private GlyphLayout glyphLayout;
    private int selected, selectedSwitch;
    private NinePatch patch;
    private ChoiceBox<String> pkmnOptions;
    private boolean selectingOptions, isSwitching;
    private Camera camera;

    /**
     * <p>
     *     Constructs a new {@code ScenePartyList}.
     * </p>
     *
     * @param app The application.
     */
    ScenePartyList(PokemonApplication app, Player player) {
        super(app);
        this.camera = app.getDefaultCamera();
        app.bgColor.set(Constants.WHITE);
        this.selected = 0;
        this.player = player;
        this.font = app.getResourceManager().get(BitmapFont.class, Constants.DEFAULT_FONT);
        this.patch = new NinePatch(
                app.getResourceManager().get(Texture.class, Constants.DEFAULT_WINDOWSKIN),
                16, 16, 16, 16);
        TextureAtlas texMisc = app.getResourceManager().get(TextureAtlas.class, Constants.DEFAULT_MISC);
        this.texCaret = texMisc.findRegion("caret");
        this.texCaretSelected = texMisc.findRegion("caret_selected");
        this.texHealthbar = texMisc.findRegion("health_bar");
        this.texHealthFill = texMisc.findRegion("health_fill");
        this.texLevelIcon = texMisc.findRegion("level_icon");
        this.glyphLayout = new GlyphLayout(font, "");
        this.pkmnOptions = new ChoiceBox<>(patch, font, this.texCaret, "STATS", "SWITCH", "CANCEL");
        this.selectingOptions = false;
        this.isSwitching = false;
    }

    @Override
    protected void update(float deltaTime) {

    }

    @Override
    protected void render(SpriteBatch batch) {
        for(int i = 0; i < this.player.getPartyCount(); i++) {
            this.renderPokemonOption(i, this.player.getParty()[i], batch);
        }
        this.patch.draw(batch, 0.0f, 0.0f, PokemonApplication.V_WIDTH, 48.0f);
        this.font.draw(batch, this.isSwitching ? "Move POKeMON\n\nwhere?" : "Choose a POKeMON.", 8f, 32.0f);
        if(this.selectingOptions) {
            this.pkmnOptions.render(batch, PokemonApplication.V_WIDTH - 72f, 0f, 72f, 56f);
        }
    }

    /**
     * <p>
     *     Renders a pokemon option.
     * </p>
     *
     * @param batch The sprite batch.
     */
    private void renderPokemonOption(int i, Pokemon p, SpriteBatch batch) {
        if(p == null)
            return;
        this.font.draw(batch, p.getName(), 24f, this.camera.viewportHeight-(i*16f));
        batch.draw(this.texHealthbar, 33f, this.camera.viewportHeight-14f-(i*16f));
        batch.draw(this.texHealthFill, 48f, this.camera.viewportHeight-13f-(i*16f),
                48f*((float)p.getHealth()/(float)p.getStat(Stat.STAT_HP)), 2f);
        batch.draw(this.texLevelIcon, 104f, this.camera.viewportHeight-8f-(i*16f));
        this.font.draw(batch, String.valueOf(p.getLevel()), 112f, this.camera.viewportHeight-(i*16f));
        this.glyphLayout.setText(this.font, String.valueOf(p.getHealth()));
        this.font.draw(batch, this.glyphLayout, PokemonApplication.V_WIDTH-32f-this.glyphLayout.width, this.camera.viewportHeight-8f-(i*16f));
        this.font.draw(batch, "/", PokemonApplication.V_WIDTH-32f, this.camera.viewportHeight-8f-(i*16f));
        this.glyphLayout.setText(this.font, String.valueOf(p.getStat(Stat.STAT_HP)));
        this.font.draw(batch, this.glyphLayout, PokemonApplication.V_WIDTH-this.glyphLayout.width, this.camera.viewportHeight-8f-(i*16f));
        if(i == selected) {
            batch.draw(this.isSwitching ? this.texCaretSelected : this.texCaret, 0.0f, this.camera.viewportHeight-16f-(i*16f));
        }
        if(this.isSwitching && i == selectedSwitch) {
            batch.draw(this.texCaret, 0.0f, this.camera.viewportHeight-16f-(i*16f));
        }
    }

    @Override
    public void dispose() {
        app.bgColor.set(Color.BLACK);
    }

    @Override
    public boolean keyDown(int keycode) {
        if(this.selectingOptions) {
            if (keycode == Input.Keys.Z) {
                if (this.pkmnOptions.getCurrentValue().equalsIgnoreCase("STATS")) {
                    app.getSceneManager().push(new ScenePokemonSummary(app, this.player.getParty()[this.selected]));
                } else if (this.pkmnOptions.getCurrentValue().equalsIgnoreCase("SWITCH")) {
                    this.selectedSwitch = this.selected;
                    this.isSwitching = true;
                    this.selectingOptions = false;
                } else if (this.pkmnOptions.getCurrentValue().equalsIgnoreCase("CANCEL")) {
                    this.selectingOptions = false;
                }
                return true;
            } else if (keycode == Input.Keys.X) {
                this.selectingOptions = false;
                return true;
            } else if (keycode == Input.Keys.UP) {
                this.pkmnOptions.setCurrentIndex(this.pkmnOptions.getCurrentIndex() - 1);
                return true;
            } else if (keycode == Input.Keys.DOWN) {
                this.pkmnOptions.setCurrentIndex(this.pkmnOptions.getCurrentIndex() + 1);
                return true;
            }
        } else if(this.isSwitching) {
            if(keycode == Input.Keys.Z) {
                this.player.switchPokemon(this.selected, this.selectedSwitch);
                this.selected = this.selectedSwitch;
                this.selectedSwitch = 0;
                this.isSwitching = false;
                return true;
            } else if(keycode == Input.Keys.X) {
                this.selectedSwitch = 0;
                this.isSwitching = false;
                return true;
            } else if(keycode == Input.Keys.UP) {
                this.selectedSwitch = Math.max(0, this.selected-1);
                return true;
            } else if(keycode == Input.Keys.DOWN) {
                this.selectedSwitch = Math.min(this.player.getPartyCount()-1, this.selected+1);
                return true;
            }
        } else {
            if(keycode == Input.Keys.Z) {
                this.pkmnOptions.setCurrentIndex(0);
                this.selectingOptions = true;
                return true;
            } else if(keycode == Input.Keys.X) {
                this.app.getSceneManager().pop();
                return true;
            } else if(keycode == Input.Keys.UP) {
                this.selected = Math.max(0, this.selected-1);
                return true;
            } else if(keycode == Input.Keys.DOWN) {
                this.selected = Math.min(this.player.getPartyCount()-1, this.selected+1);
                return true;
            }
        }
        return false;
    }
}
