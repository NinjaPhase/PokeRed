package com.ninjaphase.pokered.data.pokemon.item;

import com.ninjaphase.pokered.data.pokemon.Pokemon;
import com.ninjaphase.pokered.scene.battle.Battle;

/**
 * <p>
 *     The {@code CatchBattleAction} is used to attempt to catch a pokemon.
 * </p>
 */
public class CatchBattleAction implements ItemBattleAction {

    private float rate;

    /**
     * <p>
     *     Constructs a new {@code CatchBattleAction}.
     * </p>
     *
     * @param rate The catch rate.
     */
    public CatchBattleAction(float rate) {
        this.rate = rate;
    }

    @Override
    public void onItemUsage(Battle b, Pokemon p) {
        b.attemptCatch(rate);
    }
}
