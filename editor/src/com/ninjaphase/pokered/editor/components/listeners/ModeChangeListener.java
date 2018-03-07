package com.ninjaphase.pokered.editor.components.listeners;

import com.ninjaphase.pokered.editor.components.Mode;

/**
 * <p>
 *     The {@code ModeChangeListener} is used to activate events on mode change.
 * </p>
 */
public interface ModeChangeListener {

    /**
     * <p>
     *     Called on mode change.
     * </p>
     *
     * @param mode The mode.
     */
    void onModeChange(Mode mode);

}
