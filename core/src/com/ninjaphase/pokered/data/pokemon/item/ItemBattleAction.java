package com.ninjaphase.pokered.data.pokemon.item;

import com.ninjaphase.pokered.data.pokemon.Pokemon;
import com.ninjaphase.pokered.scene.battle.Battle;

/**
 * <p>
 *     The {@code ItemBattleAction} defines an items usage within battle.
 * </p>
 */
public interface ItemBattleAction {

    /**
     * <p>
     *     Called when an item is used within battle.
     * </p>
     *
     * @param b The battle.
     * @param p The pokemon currently out and used on.
     */
    void onItemUsage(Battle b, Pokemon p);

}
