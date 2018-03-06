package com.ninjaphase.pokered.editor.components;

import com.ninjaphase.pokered.editor.PokeredEditor;
import com.ninjaphase.pokered.editor.data.TileMap;
import com.ninjaphase.pokered.editor.data.listeners.MapChangeListener;

import javax.swing.*;
import java.awt.*;

/**
 * <p>
 *     The {@code TileSetViewer} is used to view the tileset on the map screen.
 * </p>
 */
public class TileSetViewer extends JPanel implements MapChangeListener {

    private TileMap tileMap;
    private int selectedTile;

    /**
     * <p>
     *     Constructs a new {@code TileMapViewer}.
     * </p>
     */
    public TileSetViewer() {
        PokeredEditor.getEditor().getDataManager().addMapChangeListener(this);
    }

    @Override
    public void paint(Graphics gg) {

        if(tileMap == null)
            return;

        Graphics2D g = (Graphics2D) gg;

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        int cols = 8;
        int rows = this.tileMap.getTileset().length/cols;
        for(int y = 0; y < rows; y++) {
            for(int x = 0; x < cols; x++) {
                g.drawImage(this.tileMap.getTileset()[x+(y*cols)], x*16, y*16, null);
            }
        }
    }

    @Override
    public void onMapChange(TileMap newMap) {
        this.tileMap = newMap;
        this.repaint();
    }
}
