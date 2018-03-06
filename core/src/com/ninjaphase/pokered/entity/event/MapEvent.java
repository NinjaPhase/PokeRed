package com.ninjaphase.pokered.entity.event;

/**
 * <p>
 *     A {@code MapEvent} is an event that can be queued for the map to play, to play a linear event.
 * </p>
 */
public abstract class MapEvent {

    private MapEvent next;

    /**
     * <p>
     *     The {@code MapEventPlayer} must begin.
     * </p>
     *
     * @param player The event player.
     */
    public abstract void begin(MapEventPlayer player);

    /**
     * <p>
     *     Gets when to queue the next event.
     * </p>
     */
    public abstract boolean isFinished(MapEventPlayer player);

    /**
     * <p>
     *     Sets the next event.
     * </p>
     *
     * @param next The next map event.
     */
    public void setNextEvent(MapEvent next) {
        this.next = next;
    }

    /**
     * <p>
     *     Gets the next processable event.
     * </p>
     *
     * @return The next processable event.
     */
    public MapEvent getNextEvent() {
        return this.next;
    }

}
