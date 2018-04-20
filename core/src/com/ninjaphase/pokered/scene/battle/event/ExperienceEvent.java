package com.ninjaphase.pokered.scene.battle.event;

import com.ninjaphase.pokered.data.pokemon.Pokemon;

/**
 * Created by joshua on 18/04/2018.
 */

public class ExperienceEvent extends BattleEvent {
    private static final float SPEED = 2.0f;

    private float t;
    private float oldValue;
    private float newValue;
    private int levelFor;

    private BattleEventPlayer player;

    /**
     * <p>
     *     Constructs a new {@code ExperienceEvent}.
     * </p>
     *
     * @param pokemon The pokemon to do this for.
     * @param levelFor The experience percentage for what level.
     *
     */
    public ExperienceEvent(Pokemon pokemon, int levelFor) {
        this.levelFor = levelFor;
        if(pokemon.getLevel() == levelFor) {
            newValue = pokemon.getExperiencePercentage();
        } else if(pokemon.getLevel() > levelFor) {
            newValue = 1.0f;
        }

    }

    @Override
    public void begin(BattleEventPlayer eventPlayer) {
        this.player = eventPlayer;
        this.oldValue = eventPlayer.getPlayerStatus().getExperiencePercentage();
        if(this.oldValue == 1.0f)
            this.oldValue = 0.0f;
    }

    @Override
    public void update(float deltaTime) {
        t += deltaTime * SPEED;
        float inter = oldValue + ((newValue-oldValue)*t);
        this.player.getPlayerStatus().setExperience(inter);
    }

    @Override
    public boolean isFinished() {
        return t > 1.0f;
    }
}
