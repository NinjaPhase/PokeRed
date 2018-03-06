package com.ninjaphase.pokered.scene.battle.event;

import com.badlogic.gdx.Input;
import com.ninjaphase.pokered.scene.gui.MessageBox;

/**
 * <p>
 *     The {@code TextBattleEvent} is used to display text on the battle.
 * </p>
 *
 * @see BattleEvent
 */
public class TextBattleEvent extends BattleEvent {

    private String message;

    private MessageBox messageBox;
    private boolean finished;

    /**
     * <p>
     *     Constructs a new {@code TextBattleEvent}.
     * </p>
     *
     * @param msg The message to display.
     */
    public TextBattleEvent(String msg) {
        this.message = msg;
    }

    @Override
    public void begin(BattleEventPlayer eventPlayer) {
        this.messageBox = eventPlayer.getMessageBox();
        this.messageBox.setMessage(this.message);
        this.messageBox.setVisible(true);
    }

    @Override
    public void update(float deltaTime) {
        this.messageBox.update(deltaTime);
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.Z) {
            this.finished = this.messageBox.isFinished();
            return true;
        }
        return super.keyDown(keycode);
    }
}
