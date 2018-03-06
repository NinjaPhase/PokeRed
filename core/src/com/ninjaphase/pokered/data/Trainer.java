package com.ninjaphase.pokered.data;

/**
 * <p>
 *     The {@code Trainer} class is used to hold pokemon data for a given trainer, and also contain the trainer
 *     information.
 * </p>
 */
public interface Trainer {

    /**
     * @return The {@code Trainer}'s name.
     */
    String getName();

    /**
     * @return The {@code Trainer}'s party.
     */
    Pokemon[] getParty();

    /**
     * @return The amount of pokemon in the party.
     */
    int getPartyCount();

    /**
     * @return Gets the first active (Not-fainted) pokemon.
     */
    public default Pokemon getActivePokemon() {
        for(Pokemon p : this.getParty()) {
            if(p.getHealth() == 0)
                continue;
            return p;
        }
        return null;
    }

}
