package com.ninjaphase.pokered.scene.battle.event;

import com.ninjaphase.pokered.scene.gui.EnemyStatusBox;
import com.ninjaphase.pokered.scene.gui.MessageBox;
import com.ninjaphase.pokered.scene.gui.PlayerStatusBox;

/**
 * <p>
 *     The {@code BattleEventPlayer} is used to play a sequence of events for within a battle.
 * </p>
 *
 * <p>
 *     This code is deeply inspired by hydrozoa's code, found at https://github.com/hydrozoa-yt.
 * </p>
 */
public interface BattleEventPlayer {

    /**
     * <p>
     *     Queues an event.
     * </p>
     *
     * @param ev The event to queue.
     */
    void queueEvent(BattleEvent ev);

    /**
     * @return The messagebox for the event player.
     */
    MessageBox getMessageBox();

    PlayerStatusBox getPlayerStatus();
    EnemyStatusBox getEnemyStatus();

}
