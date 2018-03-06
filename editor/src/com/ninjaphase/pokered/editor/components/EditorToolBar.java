package com.ninjaphase.pokered.editor.components;

import com.ninjaphase.pokered.editor.PokeredEditor;
import com.ninjaphase.pokered.editor.data.TileMap;
import com.ninjaphase.pokered.editor.data.listeners.MapChangeListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>
 *     The {@code EditorToolBar} is used when painting the map.
 * </p>
 */
public class EditorToolBar extends JToolBar implements ActionListener, MapChangeListener {

    private JToggleButton btnPaint, btnEvents;

    /**
     * <p>
     *     Constructs a new {@code EditorToolBar}.
     * </p>
     */
    public EditorToolBar() {
        super();
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
        this.btnEvents = new JToggleButton("Events");
        this.btnEvents.setEnabled(false);
        this.btnEvents.addActionListener(this);
        ButtonGroup grpEditing = new ButtonGroup();
        grpEditing.add(this.btnPaint);
        grpEditing.add(this.btnEvents);
        this.add(this.btnPaint);
        this.add(this.btnEvents);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void onMapChange(TileMap newMap) {
        this.btnPaint.setEnabled(newMap != null);
        this.btnEvents.setEnabled(newMap != null);
    }
}
