package com.ninjaphase.pokered.editor.components;

import com.ninjaphase.pokered.editor.PokeredEditor;
import com.ninjaphase.pokered.editor.data.TileMap;
import com.ninjaphase.pokered.editor.data.listeners.MapChangeListener;

import javax.swing.*;
import java.awt.*;

/**
 * <p>
 *     The {@code TileMapViewer} paints the tilemap onto a component.
 * </p>
 */
public class TileMapViewer extends JPanel implements MapChangeListener {

    private TileMap tileMap;

    /**
     * <p>
     *     Constructs a new {@code TileMapViewer}.
     * </p>
     */
    public TileMapViewer() {
        PokeredEditor.getEditor().getDataManager().addMapChangeListener(this);
    }

    @Override
    public void paint(Graphics g) {

        if(tileMap == null)
            return;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, tileMap.getWidth()*16, tileMap.getHeight()*16);

        tileMap.render(g);
    }

    @Override
    public void onMapChange(TileMap newMap) {
        this.tileMap = newMap;
        this.repaint();
    }
}
