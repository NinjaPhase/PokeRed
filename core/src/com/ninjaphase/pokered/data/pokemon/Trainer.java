package com.ninjaphase.pokered.data.pokemon;

import com.ninjaphase.pokered.data.pokemon.Pokemon;

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
     * <p>
     *     Heals all pokemon within the party.
     * </p>
     */
    void healParty();

    /**
     * @return Gets the first active (Not-fainted) pokemon.
     */
    Pokemon getActivePokemon();

}
