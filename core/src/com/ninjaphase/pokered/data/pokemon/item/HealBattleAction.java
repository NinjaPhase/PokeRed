package com.ninjaphase.pokered.data.pokemon.item;

import com.ninjaphase.pokered.data.pokemon.Pokemon;
import com.ninjaphase.pokered.scene.battle.Battle;

/**
 * <p>
 *     The {@code HealBattleAction} is used to heal a pokemon.
 * </p>
 */
public class HealBattleAction implements ItemBattleAction {

    private int amount;

    /**
     * <p>
     *     Constructs a new {@code HealBattleAction}.
     * </p>
     *
     * @param amount The amount.
     */
    public HealBattleAction(int amount) {
        this.amount = amount;
    }

    @Override
    public void onItemUsage(Battle b, Pokemon p) {
        p.setHealth(p.getHealth() + this.amount);
    }
}
