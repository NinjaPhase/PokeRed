package com.ninjaphase.pokered.editor.components;

import com.ninjaphase.pokered.editor.PokeredEditor;
import com.ninjaphase.pokered.editor.data.TileMap;
import com.ninjaphase.pokered.editor.data.listeners.MapChangeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

/**
 * <p>
 *     The {@code TileMapViewer} paints the tilemap onto a component.
 * </p>
 */
public class TileMapViewer extends JPanel implements MapChangeListener, MouseMotionListener {

    private TileMap tileMap;
    private TileSetViewer tileSetViewer;

    private int mouseX, mouseY;

    /**
     * <p>
     *     Constructs a new {@code TileMapViewer}.
     * </p>
     *
     * @param tileSetViewer The tileset viewer to paint with.
     */
    public TileMapViewer(TileSetViewer tileSetViewer) {
        this.tileSetViewer = tileSetViewer;
        this.mouseX = -1;
        this.mouseY = -1;
        this.addMouseMotionListener(this);
        PokeredEditor.getEditor().getDataManager().addMapChangeListener(this);
    }

    @Override
    public void paint(Graphics gg) {
        super.paint(gg);

        if(tileMap == null)
            return;

        Graphics2D g = (Graphics2D) gg;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, tileMap.getWidth()*16, tileMap.getHeight()*16);

        tileMap.render(g);

        Composite defComp = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        BufferedImage selectedTile = this.tileMap.getTileset()[this.tileSetViewer.getSelectedTile()];
        g.drawImage(selectedTile, mouseX*16, mouseY*16, null);
        g.setComposite(defComp);

        System.out.println(mouseX);
    }

    @Override
    public void onMapChange(TileMap newMap) {
        this.tileMap = newMap;
        this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mouseX = e.getX()/16;
        this.mouseY = e.getY()/16;
        this.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }
}
