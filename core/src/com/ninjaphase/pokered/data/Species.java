package com.ninjaphase.pokered.data;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import com.ninjaphase.pokered.PokemonApplication;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *     The {@code Species} handles the species of the
 * </p>
 */
public class Species {

    private int index;
    private int[] baseStat;
    private String name;
    private TextureRegion frontTex, backTex;
    private List<LearnsetPair> learnset;

    /**
     * <p>
     *     Constructs a new {@code Species}.
     * </p>
     *
     * @param index The species index.
     * @param data The species data.
     * @param frontTex The front texture of the species.
     */
    Species(int index, JsonValue data, TextureRegion frontTex, TextureRegion backTex) {
        this.learnset = new LinkedList<>();
        this.index = index;
        this.baseStat = new int[data.get("base_stat").size];
        for(int i = 0; i < this.baseStat.length; i++)
            this.baseStat[i] = data.get("base_stat").getInt(i);
        this.name = data.getString("name");
        this.frontTex = frontTex;
        this.backTex = backTex;
        for(int i = 0; i < data.get("learnset").size; i++) {
            Move m = PokemonApplication.getApplication().getDataManager().getMove(
                    data.get("learnset").get(i).getString("move"));
            this.learnset.add(new LearnsetPair(data.get("learnset").get(i).getInt("level"), m));
        }
    }

    /**
     * @param i The stat index.
     * @return The base state.
     */
    public int getBaseStat(int i) {
        return this.baseStat[i];
    }

    /**
     * @return The Species' front texture.
     */
    public TextureRegion getFrontTexture() {
        return this.frontTex;
    }

    /**
     * @return The Species' back texture.
     */
    public TextureRegion getBackTex() {
        return this.backTex;
    }

    /**
     * @return The name of the species.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return The learnset of this pokemon.
     */
    public List<LearnsetPair> getLearnset() {
        return this.learnset;
    }

    /**
     * <p>
     *     The {@code LearnsetPair} holds the data for a learnset move value.
     * </p>
     */
    public static class LearnsetPair {
        public int level;
        public Move move;

        public LearnsetPair(int level, Move move) {
            this.level = level;
            this.move = move;
        }
    }

}
