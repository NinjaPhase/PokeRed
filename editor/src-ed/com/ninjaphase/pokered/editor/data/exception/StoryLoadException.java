package com.ninjaphase.pokered.editor.data.exception;

/**
 * <p>
 *     Called if there is a problem loading the story.
 * </p>
 */
public class StoryLoadException extends Exception {

    /**
     * <p>
     *     Constructs a new {@code StoryLoadException}.
     * </p>
     *
     * @param msg The message for the exception.
     * @param t The throwable.
     */
    public StoryLoadException(String msg, Throwable t) {
        super(msg, t);
    }

}
