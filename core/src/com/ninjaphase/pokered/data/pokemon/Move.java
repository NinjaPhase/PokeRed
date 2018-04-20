package com.ninjaphase.pokered.data.pokemon;

import com.badlogic.gdx.utils.JsonValue;
import com.ninjaphase.pokered.scene.battle.event.BattleEventPlayer;
import com.ninjaphase.pokered.scene.battle.event.TextBattleEvent;

/**
 * <p>
 *     The {@code Move} class holds the data for all the moves within the game.
 * </p>
 */
public class Move {

    private int basePower, totalPP, functionCode;
    private String name;
    private Type type;

    /**
     * <p>
     *     Constructs a new {@code Move}.
     * </p>
     *
     * @param data The moves data.
     */
    public Move(JsonValue data) {
        this.name = data.getString("name");
        this.type = Type.valueOf("TYPE_" + data.getString("type").toUpperCase());
        this.basePower = data.getInt("base_power");
        this.totalPP = data.getInt("pp");
        this.functionCode = data.getInt("function_code");
    }

    /**
     * <p>
     *     Uses a move on the opponent.
     * </p>
     *
     * @param eventPlayer Used to display text.
     * @param user The user of the move.
     * @param target The target.
     */
    public void useMove(BattleEventPlayer eventPlayer, Pokemon user, Pokemon target) {
        float a = ((2f * user.getLevel())/5f)+2f;
        float b = ((a * this.basePower * (user.getEffectiveStat(Stat.STAT_ATTACK) / target.getEffectiveStat(Stat.STAT_DEFENCE))) / 50f) + 2f;

        float typeModifier = 1.0f;

        for(Type t : target.getSpecies().getTypes()) {
            typeModifier *= t.getEffectiveness(this.getType());
        }

        b *= typeModifier;

        int damage = (int)Math.floor(b);

        if(this.functionCode == 0x00) {
            target.setHealth(target.getHealth() - damage);

            if(typeModifier > 1.0f) {
                eventPlayer.queueEvent(new TextBattleEvent("It was super effective!"));
            } else if(typeModifier > 0.0f && typeModifier < 1.0f) {
                eventPlayer.queueEvent(new TextBattleEvent("It's not very effective..."));
            } else if(typeModifier == 0.0f) {
                eventPlayer.queueEvent(new TextBattleEvent("It doesn't affect " + target.getName()));
            }
        } else if(this.functionCode == 0x42) {
            boolean modified = target.modifyStage(1, -1);

            if(modified) {
                eventPlayer.queueEvent(new TextBattleEvent(target.getName() + "'s\nATTACK fell!"));
            } else {
                eventPlayer.queueEvent(new TextBattleEvent("Nothing happened!"));
            }
        } else if(this.functionCode == 0x43) {
            boolean modified = target.modifyStage(2, -1);

            if(modified) {
                eventPlayer.queueEvent(new TextBattleEvent(target.getName() + "'s\nDEFENCE fell!"));
            } else {
                eventPlayer.queueEvent(new TextBattleEvent("Nothing happened!"));
            }
        } else {
            eventPlayer.queueEvent(new TextBattleEvent(this.functionCode + " has not\nbeen implemented."));
        }
    }

    /**
     * @return The moves base power.
     */
    public int getBasePower() {
        return this.basePower;
    }

    /**
     * @return The moves total PP.
     */
    public int getTotalPP() {
        return this.totalPP;
    }

    /**
     * @return The moves name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return The moves type.
     */
    public Type getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "Move<" + this.getName() + ">";
    }

}
