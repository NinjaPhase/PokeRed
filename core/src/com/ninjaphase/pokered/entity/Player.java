package com.ninjaphase.pokered.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ninjaphase.pokered.data.TileMap;

/**
 * <p>
 *     The {@code Player} class handles the controls of the player when on the map and in-battle.
 * </p>
 */
public class Player extends HumanEntity {

    /**
     * <p>
     *     Constructs a new {@code Player}.
     * </p>
     *
     * @param texture The texture of the player.
     * @param x The x position.
     * @param y The y position.
     */
    public Player(Texture texture, TileMap map, int x, int y) {
        super(texture, map, x, y);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }

}
