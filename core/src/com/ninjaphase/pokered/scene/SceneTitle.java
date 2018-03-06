package com.ninjaphase.pokered.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ninjaphase.pokered.PokemonApplication;
import com.ninjaphase.pokered.data.Species;

/**
 * <p>
 *     The {@code SceneTitle} is used to handle the title.
 * </p>
 */
public class SceneTitle extends Scene {
    private static final float SHOWN_TIMER = 4.0f, MOVE_SPEED = 256.0f,
            STOP_LOCATION = 32.0f, TITLE_Y = 86.0f, TITLE_SPEED = 127.0f, VERSION_X = 55.0f;

    private TextureAtlas texTitleAtlas;
    private TextureRegion texLogo, texVersion;
    private Species shownSpecies;
    private float timer, speciesX, speciesVel, titleY, versionX;
    private boolean isMoving, isStopping, titleFin, versionFin;
    private BitmapFont font;

    /**
     * <p>
     *     Constructs a new {@code SceneTitle}.
     * </p>
     *
     * @param app The Pokemon Application.
     */
    public SceneTitle(PokemonApplication app) {
        super(app);

        this.font = new BitmapFont(Gdx.files.internal("font/basic_font.fnt"));
        this.texTitleAtlas = new TextureAtlas("img/title.pack");
        this.texLogo = this.texTitleAtlas.findRegion("logo");
        this.texVersion = this.texTitleAtlas.findRegion("red_version");
        this.shownSpecies = app.getDataManager().getSpecies(3);
        this.speciesX = STOP_LOCATION;
        this.titleY = 144.0f;
        this.versionX = 160.0f;
        this.isMoving = true;
        this.isStopping = false;
    }

    @Override
    void update(float deltaTime) {
        if(!titleFin) {
            this.titleY -= deltaTime * TITLE_SPEED;

            if (this.titleY <= TITLE_Y) {
                this.titleY = TITLE_Y;
                this.titleFin = true;
            }
        } else if(!versionFin) {
            this.versionX -= deltaTime * TITLE_SPEED;

            if(this.versionX <= VERSION_X) {
                this.versionX = VERSION_X;
                this.versionFin = true;
            }
        } else {
            if (isMoving) {
                this.speciesVel = Math.min(this.speciesVel + (deltaTime * 512.0f), MOVE_SPEED);
                this.speciesX -= deltaTime * speciesVel;

                if (this.isStopping) {
                    if (this.speciesX <= STOP_LOCATION) {
                        this.speciesX = STOP_LOCATION;
                        this.isMoving = false;
                        this.isStopping = false;
                    }
                } else {
                    if (this.speciesX < -32.0f) {
                        this.shownSpecies = app.getDataManager().getRandomSpecies();
                        this.speciesX = 160.0f;
                        this.isStopping = true;
                    }
                }
            } else {
                this.timer += deltaTime;
                if (this.timer >= SHOWN_TIMER) {
                    this.isMoving = true;
                    this.timer = 0.0f;
                }
            }
        }
    }

    @Override
    void render(SpriteBatch batch) {
        batch.draw(this.texTitleAtlas.findRegion("copyright"), 16.0f, 2.0f);
        batch.draw(this.texLogo, 16.0f, titleY);
        batch.draw(this.texVersion, versionX, 76.0f);
        batch.draw(this.shownSpecies.getFrontTexture(), speciesX, 8.0f);
        batch.draw(this.texTitleAtlas.findRegion("player"), 80.0f, 8.0f);
    }

    @Override
    boolean keyDown(int keycode) {
        if(keycode == Input.Keys.Z) {
            app.getSceneManager().pop();
            app.getSceneManager().push(new SceneMap(app));
            return true;
        }
        return false;
    }

    @Override
    public void dispose() {
        this.texTitleAtlas.dispose();
    }
}
