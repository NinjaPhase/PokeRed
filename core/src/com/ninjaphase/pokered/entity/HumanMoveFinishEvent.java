package com.ninjaphase.pokered.entity;

/**
 * <p>
 *     The {@code HumanMoveFinishEvent} is called when a person finishes his movement.
 * </p>
 */
public interface HumanMoveFinishEvent {

    /**
     * <p>
     *     Called on the move step.
     * </p>
     *
     * @param entity The entity.
     * @param x The x position.
     * @param y The y position.
     * @return The move step.
     */
    public boolean onMoveStep(HumanEntity entity, int x, int y);

}
