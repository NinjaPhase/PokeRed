package com.ninjaphase.pokered.scene.battle.event;

/**
 * <p>
 *     A {@code BattleEvent} is used to perform visual cues and prompts within a battle.
 * </p>
 *
 * <p>
 *     This code is deeply inspired by hydrozoa's code, found at https://github.com/hydrozoa-yt.
 * </p>
 */
public abstract class BattleEvent {

    /**
     * <p>
     *     Called when the {@code BattleEvent} has begun.
     * </p>
     *
     * @param eventPlayer The event player.
     */
    public abstract void begin(BattleEventPlayer eventPlayer);

    /**
     * <p>
     *     Updates the {@code BattleEvent}.
     * </p>
     *
     * @param deltaTime The delta time for frame operations.
     */
    public abstract void update(float deltaTime);

    /**
     * <p>
     *     Whether this {@code BattleEvent} has finished.
     * </p>
     *
     * @return Whether the event has finished.
     */
    public abstract boolean isFinished();

    public boolean keyDown(int keycode) { return false; }

}
