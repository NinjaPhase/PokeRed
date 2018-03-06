package com.ninjaphase.pokered.data;

import com.ninjaphase.pokered.PokemonApplication;

import java.util.Arrays;

/**
 * <p>
 *     Constructs a new {@code Pokemon}.
 * </p>
 */
public class Pokemon {
    private static final float[] STAGE_MODIFIERS = new float[]{
            25f/100f, 28f/100f, 33f/100f, 40f/100f, 50f/100f, 66f/100f, 100f/100f,
            150f/100f, 200f/100f, 250f/100f, 300f/100f, 350f/100f, 400f/100f
    };

    private int level, health;
    private int[] stats, ivs, evs, stages;
    private String name;
    private Species species;
    private Move[] moves;

    /**
     * <p>
     *     Constructs a new {@code Pokemon}.
     * </p>
     *
     * @param i The index of the pokemon.
     * @param level The level of the pokemon.
     */
    public Pokemon(int i, int level) {
        this(PokemonApplication.getApplication().getDataManager().getSpecies(i-1), level);
    }

    /**
     * <p>
     *     Constructs a new {@code Pokemon} from a given species.
     * </p>
     *
     * @param species The species of the pokemon.
     * @param level The level of the pokemon.
     */
    public Pokemon(Species species, int level) {
        this.moves = new Move[4];
        this.species = species;
        this.name = this.species.getName();
        this.level = level;
        this.stats = new int[5];
        this.ivs = new int[5];
        this.evs = new int[5];
        this.stages = new int[4];
        int hpIv = 0;
        for(int i = 1; i < this.ivs.length; i++) {
            this.ivs[i] = (int)Math.round(Math.random()*15f);
            hpIv = hpIv << 1;
            hpIv |= this.ivs[i] & 0x1;
        }
        this.ivs[0] = hpIv;
        for(int i = 0; i < this.stats.length; i++) {
            this.stats[i] = this.calcStat(i);
        }
        this.health = this.stats[0];

        int i = 0;
        for(Species.LearnsetPair set : species.getLearnset()) {
            if(set.level > this.level || this.hasMove(set.move))
                continue;
            this.moves[i] = set.move;
            i = (i+1) % 4;
        }
    }

    /**
     * <p>
     *     Calculates the stat for a given stat ordinal.
     * </p>
     *
     * @param i The index.
     * @return The stat at that point.
     */
    public int calcStat(int i) {
        double a = ((this.species.getBaseStat(i) + ivs[i]) * 2f + Math.floor(Math.ceil(Math.sqrt(evs[i])) / 4f)) * level;
        int b = i == 0 ? this.level + 10 : 5;

        return ((int) Math.floor(a/100f)) + b;
    }

    /**
     * <p>
     *     Resets the temporary battle data, this includes switching in and out.
     * </p>
     */
    public void resetTempData() {
        for(int i = 0; i < 4; i++)
            this.stages[i] = 0;
    }

    /**
     * @param m The move
     * @return Whether he player has the given move.
     */
    public boolean hasMove(Move m) {
        for(int i = 0; i < this.moves.length; i++) {
            if(this.moves[i] == null || this.moves[i] != m)
                continue;
            return true;
        }
        return false;
    }

    /**
     * @return The level of the pokemon.
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * <p>
     *     Sets the pokemons health value.
     * </p>
     *
     * @param health The health value.
     */
    public void setHealth(int health) {
        this.health = Math.min(Math.max(0, health), this.getStat(0));
    }

    /**
     * @return The pokemons health value.
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * @param i The stat index.
     * @return The stat at this moment in time.
     */
    public int getStat(int i) {
        return this.stats[i];
    }

    /**
     * <p>
     *     Applies stage modifiers.
     * </p>
     *
     * @param stat The stat.
     * @param amount The amount.
     * @return Whether the value was changed or not.
     */
    public boolean modifyStage(int stat, int amount) {
        int old = this.stages[stat-1];
        this.stages[stat-1] = Math.max(-6, Math.min(6, old+amount));
        return old != this.stages[stat-1];
    }

    /**
     * @param i The stat index.
     * @return The stat used for battle.
     */
    public int getEffectiveStat(int i) { return (int)(this.getStat(i)*STAGE_MODIFIERS[6+this.stages[i-1]]); }

    /**
     * @return The name of the pokemon.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return The pokemons species.
     */
    public Species getSpecies() {
        return this.species;
    }

    /**
     * @param i The move index.
     * @return The move at this position.
     */
    public Move getMove(int i) {
        return this.moves[i];
    }

    /**
     * @return The move count.
     */
    public int getMoveCount() {
        for(int i = 0; i < this.moves.length; i++) {
            if(this.moves[i] == null)
                return i;
        }
        return 4;
    }

}
