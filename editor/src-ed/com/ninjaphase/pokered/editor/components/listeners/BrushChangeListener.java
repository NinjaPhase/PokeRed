package com.ninjaphase.pokered.editor.components.listeners;

import com.ninjaphase.pokered.editor.components.brush.Brush;

/**
 * <p>
 *     Called when the brush has been changed.
 * </p>
 */
public interface BrushChangeListener {

    /**
     * <p>
     *     Sets the new brush.
     * </p>
     *
     * @param b The brush.
     */
    void onBrushChange(Brush b);

}
