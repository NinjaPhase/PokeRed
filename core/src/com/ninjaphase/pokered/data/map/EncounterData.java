package com.ninjaphase.pokered.data.map;

import com.badlogic.gdx.utils.JsonValue;
import com.ninjaphase.pokered.PokemonApplication;
import com.ninjaphase.pokered.data.pokemon.Species;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     The {@code EncounterData} handles random encounters within the game.
 * </p>
 */
public class EncounterData {

    private List<Encounter> encounterList;

    /**
     * <p>
     *     Constructs a new {@code EncounterData} from given values.
     * </p>
     */
    public EncounterData() {
        this.encounterList = new ArrayList<>();
    }

    /**
     * <p>
     *     Constructs new {@code EncounterData} from given values.
     * </p>
     *
     * @param jsonValue The json value.
     */
    public EncounterData(JsonValue jsonValue) {
        this.encounterList = new ArrayList<>();
        for(JsonValue v : jsonValue) {
            Encounter e = new Encounter();
            JsonValue species = v.get("pokemon");
            if(species.isString()) {
                e.species = PokemonApplication.getApplication().getDataManager().getSpecies(species.asString());
            } else if(species.isNumber()) {
                e.species = PokemonApplication.getApplication().getDataManager().getSpecies(species.asInt());
            } else throw new NullPointerException("Unable to get species data for encounter.");
            if(e.species == null)
                throw new NullPointerException("Species given was null " + species);
            e.weight = v.getFloat("rate");
            e.level = v.getInt("level");
            this.encounterList.add(e);
        }
        this.normaliseWeights();
    }

    /**
     * <p>
     *     Adds a given encoutner.
     * </p>
     *
     * @param id The id of the encounter.
     * @param level The level of the encounter.
     * @param weight The weight of the encounter.
     */
    public void addEncounter(int id, int level, float weight) {
        Encounter e = new Encounter();
        e.species = PokemonApplication.getApplication().getDataManager().getSpecies(id);
        e.level = level;
        e.weight = weight;
        this.encounterList.add(e);
    }

    /**
     * <p>
     *     Normalises the encounter weights.
     * </p>
     */
    public void normaliseWeights() {
        float totalWeight = 0.0f;
        for(Encounter e : this.encounterList) {
            totalWeight += e.weight;
        }
        for(Encounter e : this.encounterList) {
            e.weight = e.weight/totalWeight;
        }
    }

    /**
     * @return Gets a random encounter.
     */
    public Encounter getEncounter() {
        float current = 0.0f;
        float roll = (float)Math.random();
        for(Encounter e : this.encounterList) {
            current += e.weight;
            if(current >= roll)
                return e;
        }
        return null;
    }

    /**
     * <p>
     *     The Encounter data.
     * </p>
     */
    public static final class Encounter {
        public float weight;
        public int level;
        public Species species;
    }

}
