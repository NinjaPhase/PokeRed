package com.ninjaphase.pokered.data.pokemon;

import com.ninjaphase.pokered.PokemonApplication;

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
    private int currentExperience;

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
        this.stats = new int[6];
        this.ivs = new int[6];
        this.evs = new int[6];
        this.stages = new int[5];
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
        this.health = this.stats[Stat.STAT_HP.ordinal()];

        int i = 0;
        for(Species.LearnsetPair set : species.getLearnset()) {
            if(set.level > this.level || this.hasMove(set.move))
                continue;
            this.moves[i] = set.move;
            i = (i+1) % 4;
        }
        this.currentExperience = this.getSpecies().getExpMethod().calculateExperienceForLevel(this.level);
    }

    /**
     * @return Adds experience to this pokemon.
     */
    public int addExperience(int experience) {
        this.currentExperience += experience;
        int i = 0;
        while(this.currentExperience >= this.getSpecies().getExpMethod().calculateExperienceForLevel(this.getLevel()+1)) {
            this.level++;
            i++;
        }
        return i;
    }

    /**
     * <p>Recalculates the stats.</p>
     */
    public void recalculateStats() {
        float healthPercentage = (((float) this.getHealth()) / ((float) this.getStat(Stat.STAT_HP)));
        for(int i = 0; i < this.stats.length; i++) {
            this.stats[i] = this.calcStat(i);
        }
        this.setHealth((int)(this.getStat(Stat.STAT_HP) * healthPercentage));
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

        return ((int) Math.floor(a / 100f)) + b;
    }

    /**
     * <p>
     *     Resets the temporary battle data, this includes switching in and out.
     * </p>
     */
    public void resetTempData() {
        for(int i = 0; i < 5; i++)
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
        this.health = Math.min(Math.max(0, health), this.getStat(Stat.STAT_HP));
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

    public float getExperiencePercentage() {
        float a = this.currentExperience - this.getSpecies().getExpMethod().calculateExperienceForLevel(this.getLevel());
        float b = this.getSpecies().getExpMethod().calculateExperienceForLevel(this.getLevel()+1) - this.getSpecies().getExpMethod().calculateExperienceForLevel(this.getLevel());
        return a / b;
    }

    /**
     * @param stat The stat.
     * @return the stat at this moment in time.
     */
    public int getStat(Stat stat) {
        return this.getStat(stat.ordinal());
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
     * <p>
     *     Called to get the experience gained by this pokemon when beating the other pokemon.
     * </p>
     *
     * @param other The other pokemon.
     */
    public int getExperienceGain(Pokemon other) {
        float a = 1;
        float b = other.getSpecies().getExperienceYield();
        float t = 1;
        float L = other.getLevel();

        return (int)Math.floor((a * t * b * L) / (7f * 1.0f));
    }

    /**
     * @param i The stat index.
     * @return The stat used for battle.
     */
    public int getEffectiveStat(int i) {
        return (int)(this.getStat(i)*STAGE_MODIFIERS[6+this.stages[i-1]]);
    }

    /**
     * @param stat The stat enum.
     * @return The stat used for battle.
     */
    public int getEffectiveStat(Stat stat) {
        return this.getEffectiveStat(stat.ordinal());
    }

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
