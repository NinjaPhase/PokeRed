package com.ninjaphase.pokered.data.pokemon;

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
    private ExperienceMethod expMethod;
    private int expYield, catchRate;
    private Type[] types;

    /**
     * <p>
     *     Constructs a new {@code Species}.
     * </p>
     *
     * @param index The species index.
     * @param data The species data.
     * @param frontTex The front texture of the species.
     */
    public Species(int index, JsonValue data, TextureRegion frontTex, TextureRegion backTex) {
        this.learnset = new LinkedList<>();
        this.types = new Type[data.get("type").size];
        for(int i = 0; i < this.types.length; i++) {
            this.types[i] = Type.valueOf("TYPE_" + data.get("type").getString(i).toUpperCase());
        }
        this.index = index;
        this.frontTex = frontTex;
        this.backTex = backTex;
        this.name = data.getString("name");
        this.expYield = data.getInt("exp_yield");
        this.catchRate = data.getInt("catch_rate");
        this.baseStat = new int[data.get("base_stat").size];
        for(int i = 0; i < this.baseStat.length; i++)
            this.baseStat[i] = data.get("base_stat").getInt(i);
        if(data.get("learnset") != null) {
            for (int i = 0; i < data.get("learnset").size; i++) {
                Move m = PokemonApplication.getApplication().getDataManager().getMove(
                        data.get("learnset").get(i).getString("move"));
                if(m == null)
                    throw new NullPointerException("Given move was null " + data.get("learnset").get(i).getString("move"));
                this.learnset.add(new LearnsetPair(data.get("learnset").get(i).getInt("level"), m));
            }
        }
        switch(data.getString("experience")) {
            case "FAST":
                this.expMethod = ExperienceMethod.FAST;
                break;
            case "MEDIUM_FAST":
                this.expMethod = ExperienceMethod.MEDIUM_FAST;
                break;
            case "MEDIUM_SLOW":
                this.expMethod = ExperienceMethod.MEDIUM_SLOW;

            case "SLOW":
                this.expMethod = ExperienceMethod.SLOW;
                break;
        }
    }

    public int getIndex() {
        return this.index;
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
     * @return The id of the {@code Species}.
     */
    public int getId() {
        return this.index;
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
     * @return The experience method used by this pokemon.
     */
    public ExperienceMethod getExpMethod() {
        return this.expMethod;
    }

    /**
     * @return The amount of experience given to the other pokemon.
     */
    public int getExperienceYield() {
        return this.expYield;
    }

    /**
     * @return The catch rate of the pokemon.
     */
    public int getCatchRate() {
        return this.catchRate;
    }

    /**
     * @return The types.
     */
    public Type[] getTypes() {
        return this.types;
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
