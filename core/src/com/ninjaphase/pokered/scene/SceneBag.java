package com.ninjaphase.pokered.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.ninjaphase.pokered.PokemonApplication;
import com.ninjaphase.pokered.entity.Player;

/**
 * <p>
 *     The {@code SceneBag} is used to display the bag to the player.
 * </p>
 */
class SceneBag extends Scene {
    private static final String[] POCKET_IMAGE = new String[]{"bag_left", "bag_center", "bag_right"};

    private int bagPocket;

    private TextureAtlas bagImages;

    /**
     * <p>
     *     Constructs a new {@code SceneBag}.
     * </p>
     *
     * @param app The application.
     * @param p The player.
     */
    SceneBag(PokemonApplication app, Player p) {
        super(app);
        this.bagImages = app.getResourceManager().add("bag", new TextureAtlas(Gdx.files.internal("img/system/bag_screen.pack")));
    }

    @Override
    protected void update(float deltaTime) {

    }

    @Override
    protected void render(SpriteBatch batch) {
        batch.draw(bagImages.findRegion("bag_bg"), 0.0f, 0.0f);
        batch.draw(bagImages.findRegion(POCKET_IMAGE[bagPocket]), 12.0f, 80.0f);
    }

    @Override
    public void dispose() {
        app.getResourceManager().remove(Texture.class, "bag_bg");
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.LEFT) {
            bagPocket = Math.max(0, bagPocket - 1);
            return true;
        } else if(keycode == Input.Keys.RIGHT) {
            bagPocket = Math.min(2, bagPocket + 1);
            return true;
        }
        return false;
    }

}
