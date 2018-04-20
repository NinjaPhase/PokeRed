package com.ninjaphase.pokered.editor.data.exception;

/**
 * <p>
 *     A {@code StoryWriteException} is thrown if there is a problem writing the story.
 * </p>
 */
public class StoryWriteException extends Exception {

    /**
     * <p>
     *     Constructs a new {@code StoryWriteException}.
     * </p>
     *
     * @param message The message.
     */
    public StoryWriteException(String message) {
        super(message);
    }

    /**
     * <p>
     *     Constructs a new {@code StoryWriteException}.
     * </p>
     *
     * @param message The message.
     * @param throwable The throwable from another exception.
     */
    public StoryWriteException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
