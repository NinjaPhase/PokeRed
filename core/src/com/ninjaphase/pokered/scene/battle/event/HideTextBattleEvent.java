package com.ninjaphase.pokered.scene.battle.event;

/**
 * <p>
 *     The {@code HideTextBattleEvent} hides the textbox after usage.
 * </p>
 */
public class HideTextBattleEvent extends BattleEvent {

    @Override
    public void begin(BattleEventPlayer eventPlayer) {
        eventPlayer.getMessageBox().setVisible(false);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
