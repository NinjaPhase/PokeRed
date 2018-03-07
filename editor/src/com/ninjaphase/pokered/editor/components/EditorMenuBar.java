package com.ninjaphase.pokered.editor.components;

import com.ninjaphase.pokered.editor.PokeredEditor;
import com.ninjaphase.pokered.editor.data.TileMap;
import com.ninjaphase.pokered.editor.data.listeners.MapChangeListener;
import com.ninjaphase.pokered.editor.dialogs.NewMapDialog;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;

/**
 * <p>
 *     The {@code EditorMenuBar} is the bar in which the options will be displayed.
 * </p>
 */
public class EditorMenuBar extends JMenuBar implements ActionListener, MapChangeListener {

    private JMenu jmFile;
    private JMenuItem jmiNew, jmiSave, jmiSaveAs, jmiLoad, jmiClose, jmiExit;

    private JFileChooser jFileChooser;

    private TileMap map;

    /**
     * <p>
     *     Constructs a new {@code EditorMenuBar}.
     * </p>
     */
    public EditorMenuBar() {
        super();
        this.jFileChooser = new JFileChooser();
        if(PokeredEditor.getEditor().getPreferences().getValues().optString("last_cwd") != null) {
            this.jFileChooser.setCurrentDirectory(
                    new File(PokeredEditor.getEditor().getPreferences().getValues().optString("last_cwd"))
            );
        }
        this.jFileChooser.setFileFilter(new FileNameExtensionFilter("JSON File", "json"));
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
            this.jmiNew.setMnemonic(KeyEvent.VK_N);
            this.jmiNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
            this.jmFile.add(this.jmiLoad = createMenuItem("Open...", true));
            this.jmiLoad.setMnemonic(KeyEvent.VK_O);
            this.jmiLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
            this.jmFile.add(this.jmiClose = createMenuItem("Close", false));
            this.jmFile.addSeparator();
            this.jmFile.add(this.jmiSave = createMenuItem("Save", false));
            this.jmiSave.setMnemonic(KeyEvent.VK_S);
            this.jmiSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
            this.jmFile.add(this.jmiSaveAs = createMenuItem("Save As...", false));
            this.jmiSaveAs.setMnemonic(KeyEvent.VK_S);
            this.jmiSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
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
            NewMapDialog newMapDialog = new NewMapDialog();
            newMapDialog.setVisible(true);
            if(newMapDialog.getTileMap() != null) {
                PokeredEditor.getEditor().getDataManager().setTileMap(newMapDialog.getTileMap());
            }
        } else if(e.getSource() == this.jmiLoad) {
            if (this.jFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = this.jFileChooser.getSelectedFile();
                PokeredEditor.getEditor().getDataManager().loadTileMap(f);
            }
        } else if(e.getSource() == this.jmiSave || e.getSource() == this.jmiSaveAs) {
            File f = null;
            if(e.getSource() == this.jmiSave && this.map.getFile() != null) {
                f = this.map.getFile();
            } else {
                if(this.jFileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    f = this.jFileChooser.getSelectedFile();
                    if(!f.getName().endsWith(".json"))
                        f = new File(f.getPath() + ".json");
                }
            }
            if(f == null)
                return;
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(f));
                bw.write(this.map.toJSONString());
                bw.flush();
            } catch (IOException exc) {
                System.err.println("Unable to save file.");
            } finally {
                this.map.setFile(f);
                PokeredEditor.getEditor().getPreferences().putString("last_open", f.getAbsolutePath());
                PokeredEditor.getEditor().getPreferences().putString("last_cwd", f.getParent());
                PokeredEditor.getEditor().getPreferences().save();
                if(bw != null) {
                    try {
                        bw.close();
                    } catch (IOException exc) {
                        System.err.println("Unable to close buffered writer.");
                    }
                }
            }
        } else if(e.getSource() == this.jmiClose) {
            PokeredEditor.getEditor().getDataManager().setTileMap(null);
            PokeredEditor.getEditor().getPreferences().putNull("last_open");
            PokeredEditor.getEditor().getPreferences().save();
        } else if(e.getSource() == this.jmiExit) {
            System.exit(0);
        }
    }

    @Override
    public void onMapChange(TileMap newMap) {
        this.jmiClose.setEnabled(newMap != null);
        this.jmiSave.setEnabled(newMap != null);
        this.jmiSaveAs.setEnabled(newMap != null);
        this.map = newMap;
    }
}
