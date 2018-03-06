package com.ninjaphase.pokered.editor;

import com.ninjaphase.pokered.editor.components.EditorMenuBar;
import com.ninjaphase.pokered.editor.components.EditorToolBar;
import com.ninjaphase.pokered.editor.components.TileMapViewer;
import com.ninjaphase.pokered.editor.components.TileSetViewer;
import com.ninjaphase.pokered.editor.data.DataManager;

import javax.swing.*;
import java.awt.*;

/**
 * <p>
 *     The {@code PokeredEditor} is used to create maps on the PokeRed application.
 * </p>
 */
public class PokeredEditor extends JFrame {

    private JSplitPane splitPane;

    private DataManager dataManager;
    private int mouseX, mouseY;

    /**
     * <p>
     *     Constructs a new {@code PokeredEditor}.
     * </p>
     */
    private PokeredEditor() {
        super("Pokered Editor");
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
        this.setVisible(true);
    }

    /**
     * <p>
     *     Adds the default components.
     * </p>
     */
    private void addComponents() {
        this.splitPane = new JSplitPane();
        TileSetViewer setViewer = new TileSetViewer();
        this.splitPane.setLeftComponent(new TileSetViewer());
        this.splitPane.setRightComponent(new TileMapViewer(setViewer));
        this.setJMenuBar(new EditorMenuBar());
        this.getContentPane().add(new EditorToolBar(), BorderLayout.NORTH);
        this.getContentPane().add(this.splitPane);
    }

    /**
     * @return The applications data management.
     */
    public DataManager getDataManager() {
        return this.dataManager;
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
