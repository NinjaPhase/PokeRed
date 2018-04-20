package com.ninjaphase.pokered.scene.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ninjaphase.pokered.data.pokemon.Pokemon;
import com.ninjaphase.pokered.data.pokemon.Stat;

/**
 * <p>
 *     The {@code PlayerStatusBox} displays the players health.
 * </p>
 */
public class PlayerStatusBox implements StatusBox {

    private TextureRegion background, hpGreen, hpYellow, hpRed, expFill;

    private Pokemon pokemon;
    private int displayedHealth;
    private float displayedExperiencePercentage;
    private int pokemonLevel;
    private BitmapFont font;

    /**
     * <p>
     *     Constructs a new {@code PlayerStatusBox}.
     * </p>
     *
     * @param battleTextures The battle textures.
     */
    public PlayerStatusBox(Pokemon pokemon, BitmapFont font, TextureAtlas battleTextures) {
        this.background = battleTextures.findRegion("player_box");
        this.hpGreen = battleTextures.findRegion("health_fill");
        this.hpYellow = battleTextures.findRegion("health_fill_med");
        this.hpRed = battleTextures.findRegion("health_fill_low");
        this.expFill = battleTextures.findRegion("exp_fill");
        this.font = font;
        this.pokemon = pokemon;
        this.pokemonLevel = pokemon.getLevel();
        this.displayedHealth = pokemon.getHealth();
        this.displayedExperiencePercentage = pokemon.getExperiencePercentage();
    }

    @Override
    public void render(SpriteBatch batch, float x, float y) {
        batch.draw(this.background, x, y);
        float perc = Math.max((float)this.getHealth() / (float)this.pokemon.getStat(Stat.STAT_HP), 0.0f);
        if(perc > 0.5f) {
            batch.draw(this.hpGreen, x + 48f, y + 17f, (float) Math.ceil(48f * perc), 3);
        } else if(perc > 0.2f && perc <= 0.5f) {
            batch.draw(this.hpYellow, x + 48f, y + 17f, (float) Math.ceil(48f * perc), 3);
        } else {
            batch.draw(this.hpRed, x + 48f, y + 17f, (float) Math.ceil(48f * perc), 3);
        }
        batch.draw(this.expFill, x+32f, y+2f, (float)Math.ceil(64f*this.displayedExperiencePercentage), 2);
        this.font.draw(batch, this.pokemon.getName(), x+17f, y+34f);
        this.font.draw(batch, "\u13b8" + this.pokemonLevel, x+83f, y+34f);
        this.font.draw(batch, this.getHealth() + "/" + this.pokemon.getStat(Stat.STAT_HP), x+66f, y+16f);
    }

    /**
     * <p>
     *     Sets the new experience fill point.
     * </p>
     *
     * @param exp The experience percentage.
     */
    public void setExperience(float exp) {
        this.displayedExperiencePercentage = Math.min(Math.max(0, exp), 1.0f);
    }

    public float getExperiencePercentage() {
        return this.displayedExperiencePercentage;
    }

    @Override
    public void setHealth(int newValue) {
        this.displayedHealth = Math.min(Math.max(0, newValue), pokemon.getStat(Stat.STAT_HP));
    }

    @Override
    public int getHealth() {
        return this.displayedHealth;
    }
}
