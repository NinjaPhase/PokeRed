package com.ninjaphase.pokered.editor.data.listeners;

import com.ninjaphase.pokered.editor.data.story.Story;

/**
 * <p>
 *     The {@code StoryChangeListener} is used to detect when the currently edited story has changed.
 * </p>
 */
public interface StoryChangeListener {

    /**
     * <p>
     *     Called when the story has been changed.
     * </p>
     *
     * @param s The story.
     */
    public void onStoryChange(Story s);

}
