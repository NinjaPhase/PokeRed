package com.ninjaphase.pokered.editor.components;

import com.ninjaphase.pokered.editor.PokeredEditor;
import com.ninjaphase.pokered.editor.components.listeners.BrushChangeListener;
import com.ninjaphase.pokered.editor.components.listeners.ModeChangeListener;
import com.ninjaphase.pokered.editor.data.TileMap;
import com.ninjaphase.pokered.editor.data.listeners.MapChangeListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *     The {@code EditorToolBar} is used when painting the map.
 * </p>
 */
public class EditorToolBar extends JToolBar implements ActionListener, MapChangeListener {

    private JToggleButton btnPaint, btnCollisions, btnEvents;
    private List<BrushChangeListener> brushChangeListeners;
    private List<ModeChangeListener>  modeChangeListeners;

    /**
     * <p>
     *     Constructs a new {@code EditorToolBar}.
     * </p>
     */
    public EditorToolBar() {
        super();
        this.brushChangeListeners = new LinkedList<>();
        this.modeChangeListeners = new LinkedList<>();
        this.setFloatable(false);
        this.addComponents();
        PokeredEditor.getEditor().getDataManager().addMapChangeListener(this);
    }

    /**
     * <p>
     *     Adds a new component.
     * </p>
     */
    private void addComponents() {
        this.btnPaint = new JToggleButton("Paint");
        this.btnPaint.setEnabled(false);
        this.btnPaint.setSelected(true);
        this.btnPaint.addActionListener(this);
        this.btnCollisions = new JToggleButton("Collisions");
        this.btnCollisions.setEnabled(false);
        this.btnCollisions.addActionListener(this);
        this.btnEvents = new JToggleButton("Events");
        this.btnEvents.setEnabled(false);
        this.btnEvents.addActionListener(this);
        ButtonGroup grpEditing = new ButtonGroup();
        grpEditing.add(this.btnPaint);
        grpEditing.add(this.btnCollisions);
        grpEditing.add(this.btnEvents);
        this.add(this.btnPaint);
        this.add(this.btnCollisions);
        this.add(this.btnEvents);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(ModeChangeListener mcl : this.modeChangeListeners) {
            if(e.getSource() == this.btnPaint) {
                mcl.onModeChange(Mode.PAINT);
            } else if(e.getSource() == this.btnCollisions) {
                mcl.onModeChange(Mode.COLLISIONS);
            } else if(e.getSource() == this.btnEvents) {
                mcl.onModeChange(Mode.EVENTS);
            }
        }
    }

    @Override
    public void onMapChange(TileMap newMap) {
        this.btnPaint.setEnabled(newMap != null);
        this.btnCollisions.setEnabled(newMap != null);
        this.btnEvents.setEnabled(newMap != null);
    }

    /**
     * <p>
     *     Changes the mode listener.
     * </p>
     *
     * @param listener The listener.
     */
    public void addModeChangeListener(ModeChangeListener listener) {
        this.modeChangeListeners.add(listener);
    }

    /**
     * <p>
     *     Adds a brush change listener.
     * </p>
     *
     * @param listener The listener.
     */
    public void addBrushChangeListener(BrushChangeListener listener) {
        this.brushChangeListeners.add(listener);
    }
}
