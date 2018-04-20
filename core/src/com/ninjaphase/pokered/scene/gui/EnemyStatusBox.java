package com.ninjaphase.pokered.scene.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ninjaphase.pokered.data.pokemon.Pokemon;
import com.ninjaphase.pokered.data.pokemon.Stat;

/**
 * <p>
 *     The {@code EnemyStatusBox} is used to display the enemy details.
 * </p>
 */
public class EnemyStatusBox implements StatusBox {

    private int pokemonLevel, pokemonHP, pokemonMaxHP;
    private String pokemonName;

    private BitmapFont font;
    private TextureRegion background, hpGreen, hpYellow, hpRed;

    /**
     * <p>
     *     Constructs a new {@code EnemyStatusBox}.
     * </p>
     *
     * @param pokemon The enemy pokemon.
     * @param font The font.
     * @param battleTextures The battle textures.
     */
    public EnemyStatusBox(Pokemon pokemon, BitmapFont font, TextureAtlas battleTextures) {
        this.font = font;
        this.background = battleTextures.findRegion("enemy_box");
        this.hpGreen = battleTextures.findRegion("health_fill");
        this.hpYellow = battleTextures.findRegion("health_fill_med");
        this.hpRed = battleTextures.findRegion("health_fill_low");
        this.pokemonName = pokemon.getName();
        this.pokemonLevel = pokemon.getLevel();
        this.pokemonHP = pokemon.getHealth();
        this.pokemonMaxHP = pokemon.getStat(Stat.STAT_HP);
    }

    @Override
    public void render(SpriteBatch batch, float x, float y) {
        batch.draw(this.background, x, y);
        font.draw(batch, pokemonName, x+7f, y+26f);
        font.draw(batch, "\u13b8" + String.valueOf(pokemonLevel), x+74f, y+26f);
        float perc = Math.max((float)this.getHealth() / (float)this.pokemonMaxHP, 0.0f);
        if(perc > 0.5f) {
            batch.draw(this.hpGreen, x + 39f, y + 9f, (float) Math.ceil(48f * perc), 3);
        } else if(perc > 0.2 && perc <= 0.5f) {
            batch.draw(this.hpYellow, x + 39f, y + 9f, (float) Math.ceil(48f * perc), 3);
        } else {
            batch.draw(this.hpRed, x + 39f, y + 9f, (float) Math.ceil(48f * perc), 3);
        }
    }

    @Override
    public void setHealth(int newValue) {
        this.pokemonHP = Math.min(Math.max(0, newValue), this.pokemonMaxHP);
    }

    @Override
    public int getHealth() {
        return this.pokemonHP;
    }
}
