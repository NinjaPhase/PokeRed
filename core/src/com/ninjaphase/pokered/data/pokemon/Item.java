package com.ninjaphase.pokered.data.pokemon;

import com.badlogic.gdx.utils.JsonValue;
import com.ninjaphase.pokered.data.pokemon.item.CatchBattleAction;
import com.ninjaphase.pokered.data.pokemon.item.HealBattleAction;
import com.ninjaphase.pokered.data.pokemon.item.ItemBattleAction;

/**
 * <p>
 *     The {@code Item} class defines an item which can be used in-game.
 * </p>
 */
public class Item {

    private String singleName, pluralName;
    private String internalName;
    private ItemBattleAction battleAction;

    /**
     * <p>
     *     Constructs a new {@code Item}.
     * </p>
     *
     * @param data The item data.
     */
    public Item(JsonValue data) {
        this.internalName = data.name;
        this.singleName = data.get("name").getString(0);
        this.pluralName = data.get("name").getString(1);
        this.parseAction(data);
    }

    /**
     * <p>
     *     Parses the battle action of the item.
     * </p>
     *
     * @param data The data of the action.
     */
    private void parseAction(JsonValue data) {
        if(data.getString(0).equalsIgnoreCase("standard")) {
            JsonValue params = data.get(1);
            if(params.getString("event").equalsIgnoreCase("heal")) {
                this.battleAction = new HealBattleAction(params.getInt("amount"));
            } else if(params.getString("event").equalsIgnoreCase("catch")) {
                this.battleAction = new CatchBattleAction(params.getFloat("rate"));
            } else throw new IllegalArgumentException("Unknown standard event type " + params.getString("event"));
        } else {
            throw new IllegalArgumentException("custom events unavailable for now.");
        }
    }

    /**
     * @return Whether the item can be used in battle.
     */
    public boolean isUsedInBattle() {
        return this.battleAction != null;
    }

}
