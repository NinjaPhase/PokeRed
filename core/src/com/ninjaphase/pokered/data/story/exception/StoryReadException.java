package com.ninjaphase.pokered.data.story.exception;

/**
 * <p>
 *     A {@code StoryReadException} is thrown if there is an error when loading a story.
 * </p>
 */
public class StoryReadException extends Exception {

    /**
     * <p>
     *     Constructs a new {@code StoryReadException}.
     * </p>
     *
     * @param message The message.
     */
    public StoryReadException(String message) {
        super(message);
    }

    /**
     * <p>
     *     Constructs a new {@code StoryReadException} from a given exception.
     * </p>
     *
     * @param message The message.
     * @param throwable The throwable.
     */
    public StoryReadException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
