package com.ninjaphase.pokered.editor;

import com.ninjaphase.pokered.editor.components.EditorMenuBar;
import com.ninjaphase.pokered.editor.components.EditorToolBar;
import com.ninjaphase.pokered.editor.components.TileMapViewer;
import com.ninjaphase.pokered.editor.components.TileSetViewer;
import com.ninjaphase.pokered.editor.data.DataManager;
import com.ninjaphase.pokered.editor.data.Preferences;
import com.ninjaphase.pokered.editor.data.TileMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * <p>
 *     The {@code PokeredEditor} is used to create maps on the PokeRed application.
 * </p>
 */
public class PokeredEditor extends JFrame {

    private Preferences preferences;
    private DataManager dataManager;

    /**
     * <p>
     *     Constructs a new {@code PokeredEditor}.
     * </p>
     */
    private PokeredEditor() {
        super("Pokered Editor");
        if(!Preferences.PREFERENCES_PATH.exists()) {
            this.preferences = new Preferences();
        } else {
            this.preferences = new Preferences(Preferences.PREFERENCES_PATH);
        }
    }

    /**
     * Starts the application.
     */
    private void start() {
        this.dataManager = new DataManager();
        this.addComponents();
        Rectangle screen = this.getGraphicsConfiguration().getBounds();
        this.setSize(640, 480);
        this.setLocation(
                screen.x + (screen.width - this.getWidth()) / 2,
                screen.y + (screen.height - this.getHeight()) / 2
        );
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        if(this.preferences.getValues().optString("last_open") != null) {
            File f = new File(this.preferences.getValues().optString("last_open"));
            this.getDataManager().loadTileMap(f);
        }

        this.setVisible(true);
    }

    /**
     * <p>
     *     Adds the default components.
     * </p>
     */
    private void addComponents() {
        JScrollPane tileViewerScroll = new JScrollPane();
        TileSetViewer setViewer = new TileSetViewer(tileViewerScroll);
        tileViewerScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tileViewerScroll.setViewportView(setViewer);
        tileViewerScroll.setPreferredSize(
                new Dimension((8*16) + tileViewerScroll.getVerticalScrollBar().getWidth(), 16));
        JScrollPane mapViewerScroll = new JScrollPane();
        TileMapViewer mapViewer = new TileMapViewer(mapViewerScroll, setViewer);
        mapViewerScroll.setViewportView(mapViewer);
        this.setJMenuBar(new EditorMenuBar());
        EditorToolBar toolBar = new EditorToolBar();
        toolBar.addBrushChangeListener(mapViewer);
        toolBar.addModeChangeListener(mapViewer);
        this.getContentPane().add(toolBar, BorderLayout.NORTH);
        this.getContentPane().add(tileViewerScroll, BorderLayout.WEST);
        this.getContentPane().add(mapViewerScroll, BorderLayout.CENTER);
    }

    /**
     * @return The applications data management.
     */
    public DataManager getDataManager() {
        return this.dataManager;
    }

    /**
     * @return The applications preferences.
     */
    public Preferences getPreferences() {
        return this.preferences;
    }

    private static PokeredEditor SINGLETON = new PokeredEditor();

    /**
     * @return The editor singleton.
     */
    public static PokeredEditor getEditor() {
        return SINGLETON;
    }

    /**
     * <p>
     *     Application Entry.
     * </p>
     * @param args The application arguments.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Unable to set default look and feel.");
        }
        PokeredEditor.getEditor().start();
    }
}
