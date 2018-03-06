package com.ninjaphase.pokered.editor.components;

import com.ninjaphase.pokered.editor.PokeredEditor;
import com.ninjaphase.pokered.editor.data.TileMap;
import com.ninjaphase.pokered.editor.data.listeners.MapChangeListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>
 *     The {@code EditorMenuBar} is the bar in which the options will be displayed.
 * </p>
 */
public class EditorMenuBar extends JMenuBar implements ActionListener, MapChangeListener {

    private JMenu jmFile;
    private JMenuItem jmiNew, jmiSave, jmiSaveAs, jmiLoad, jmiClose, jmiExit;

    /**
     * <p>
     *     Constructs a new {@code EditorMenuBar}.
     * </p>
     */
    public EditorMenuBar() {
        super();
        this.addComponents();
        PokeredEditor.getEditor().getDataManager().addMapChangeListener(this);
    }

    /**
     * <p>
     *     Adds the default components.
     * </p>
     */
    private void addComponents() {
        this.jmFile = new JMenu("File");
        {
            this.jmFile.add(this.jmiNew = createMenuItem("New...", true));
            this.jmFile.add(this.jmiLoad = createMenuItem("Open...", true));
            this.jmFile.add(this.jmiClose = createMenuItem("Close", false));
            this.jmFile.addSeparator();
            this.jmFile.add(this.jmiSave = createMenuItem("Save", false));
            this.jmFile.add(this.jmiSaveAs = createMenuItem("Save As...", false));
            this.jmFile.addSeparator();
            this.jmFile.add(this.jmiExit = createMenuItem("Exit", true));
        }
        this.add(this.jmFile);
    }

    /**
     * @param text The text.
     * @param enabled Whether the component is enabled.
     * @return The new component.
     */
    private JMenuItem createMenuItem(String text, boolean enabled) {
        JMenuItem i = new JMenuItem(text);
        i.setEnabled(enabled);
        i.addActionListener(this);
        return i;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.jmiNew) {
            PokeredEditor.getEditor().getDataManager().setTileMap(new TileMap(8, 8));
        } else if(e.getSource() == this.jmiClose) {
            PokeredEditor.getEditor().getDataManager().setTileMap(null);
        } else if(e.getSource() == this.jmiExit) {
            System.exit(0);
        }
    }

    @Override
    public void onMapChange(TileMap newMap) {
        this.jmiClose.setEnabled(newMap != null);
        this.jmiSave.setEnabled(newMap != null);
        this.jmiSaveAs.setEnabled(newMap != null);
    }
}
