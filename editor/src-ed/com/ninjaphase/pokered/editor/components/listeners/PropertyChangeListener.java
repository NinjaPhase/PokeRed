package com.ninjaphase.pokered.editor.components.listeners;

import com.ninjaphase.pokered.editor.components.ViewerProperty;

/**
 * <p>
 *     The {@code PropertyChangeListener} is used to change an editor related property.
 * </p>
 */
public interface PropertyChangeListener {

    /**
     * <p>
     *     Called when the property is changed.
     * </p>
     *
     * @param property The changed property.
     * @param value The value.
     */
    void onPropertyChange(ViewerProperty property, Object value);

}
