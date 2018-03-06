package com.ninjaphase.pokered.editor.components;

import com.ninjaphase.pokered.editor.PokeredEditor;
import com.ninjaphase.pokered.editor.data.TileMap;
import com.ninjaphase.pokered.editor.data.listeners.MapChangeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * <p>
 *     The {@code TileSetViewer} is used to view the tileset on the map screen.
 * </p>
 */
public class TileSetViewer extends JPanel implements MapChangeListener, MouseListener {
    private static final Color BACKGROUND_COLOR = new Color(0xFF00FF);
    private static final Color SELECTED_COLOR = new Color(0x33CCFF);

    private int selectedTile;
    private TileMap tileMap;

    /**
     * <p>
     *     Constructs a new {@code TileMapViewer}.
     * </p>
     */
    public TileSetViewer() {
        this.addMouseListener(this);
        PokeredEditor.getEditor().getDataManager().addMapChangeListener(this);
        this.setMinimumSize(new Dimension(8*16, 16));
    }

    @Override
    public void paint(Graphics gg) {

        if(tileMap == null)
            return;

        Graphics2D g = (Graphics2D) gg;

        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        int cols = 8;
        int rows = this.tileMap.getTileset().length/cols;
        for(int y = 0; y < rows; y++) {
            for(int x = 0; x < cols; x++) {
                g.drawImage(this.tileMap.getTileset()[x+(y*cols)], x*16, y*16, null);
            }
        }

        Composite defComp = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        g.setColor(SELECTED_COLOR);
        g.fillRect((selectedTile%8)*16, (selectedTile/8)*16, 16, 16);
        g.setComposite(defComp);
    }

    @Override
    public void onMapChange(TileMap newMap) {
        this.tileMap = newMap;
        this.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(this.tileMap == null)
            return;
        int x = e.getX()/16;
        int y = e.getY()/16;
        if(x < 0 || x >= 8 || y < 0 || y >= this.tileMap.getTileset().length/8)
            return;
        int selectedId = x + (y * 8);
        if(selectedId >= this.tileMap.getTileset().length)
            return;
        this.selectedTile = selectedId;
        this.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * @return The selected tile.
     */
    int getSelectedTile() {
        return this.selectedTile;
    }

}
