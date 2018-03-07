package com.ninjaphase.pokered.editor.components;

import com.ninjaphase.pokered.editor.PokeredEditor;
import com.ninjaphase.pokered.editor.components.brush.Brush;
import com.ninjaphase.pokered.editor.components.listeners.BrushChangeListener;
import com.ninjaphase.pokered.editor.components.listeners.ModeChangeListener;
import com.ninjaphase.pokered.editor.data.TileMap;
import com.ninjaphase.pokered.editor.data.entity.Entity;
import com.ninjaphase.pokered.editor.data.listeners.MapChangeListener;
import com.ninjaphase.pokered.editor.dialogs.EventEditDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * <p>
 *     The {@code TileMapViewer} paints the tilemap onto a component.
 * </p>
 */
public class TileMapViewer extends JPanel
        implements MapChangeListener, MouseListener, MouseMotionListener, BrushChangeListener, ModeChangeListener {

    private TileMap tileMap;
    private TileSetViewer tileSetViewer;

    private int mouseX, mouseY;
    private JScrollPane scrollPane;

    private Brush brush;
    private Mode mode;

    /**
     * <p>
     *     Constructs a new {@code TileMapViewer}.
     * </p>
     *
     * @param scrollPane The scrollpane for when this has been resized.
     * @param tileSetViewer The tileset viewer to paint with.
     */
    public TileMapViewer(JScrollPane scrollPane, TileSetViewer tileSetViewer) {
        this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.brush = Brush.PENCIL_BRUSH;
        this.mode = Mode.PAINT;
        this.scrollPane = scrollPane;
        this.tileSetViewer = tileSetViewer;
        this.mouseX = -1;
        this.mouseY = -1;
        this.addMouseListener(this);
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

        if(mode == Mode.PAINT) {
            if(this.mouseX != -1 && this.mouseY != -1) {
                Composite defComp = g.getComposite();
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g.setColor(Color.WHITE);
                BufferedImage selectedTile = this.tileMap.getTileset()[this.tileSetViewer.getSelectedTile()];
                g.fillRect(mouseX*16, mouseY*16, 16, 16);
                g.drawImage(selectedTile, mouseX * 16, mouseY * 16, null);
                g.setComposite(defComp);
            }
        } else if(mode == Mode.COLLISIONS) {
            Composite defComp = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            tileMap.renderCollisions(g, Color.BLACK);
            g.setComposite(defComp);
        } else if(mode == Mode.EVENTS) {
            Composite defComp = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            tileMap.renderEntities(g);
            g.setComposite(defComp);
        }

        this.paintBorder(gg);
    }

    @Override
    public void onMapChange(TileMap newMap) {
        this.tileMap = newMap;
        if(this.tileMap != null) {
            this.setPreferredSize(new Dimension(this.tileMap.getWidth() * 16, this.tileMap.getHeight() * 16));
        } else {
            this.setPreferredSize(new Dimension(16, 16));
        }
        scrollPane.revalidate();
        scrollPane.repaint();
        this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(this.tileMap == null)
            return;
        this.mouseX = e.getX()/16;
        this.mouseY = e.getY()/16;

        if(this.mouseX < 0 || this.mouseX >= this.tileMap.getWidth())
            this.mouseX = -1;
        if(this.mouseY < 0 || this.mouseY >= this.tileMap.getHeight())
            this.mouseY = -1;
        this.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(this.tileMap == null)
            return;
        this.mouseX = e.getX()/16;
        this.mouseY = e.getY()/16;

        if(this.mouseX < 0 || this.mouseX >= this.tileMap.getWidth())
            this.mouseX = -1;
        if(this.mouseY < 0 || this.mouseY >= this.tileMap.getHeight())
            this.mouseY = -1;

        if(this.mouseX == -1 || this.mouseY == -1)
            return;

        if(this.mode == Mode.PAINT) {
            this.brush.onBrushMove(this.tileMap, this.tileSetViewer.getSelectedTile(), this.mouseX, this.mouseY);
        } else if(this.mode == Mode.COLLISIONS) {
            this.tileMap.setCollisionAt(!e.isShiftDown(), this.mouseX, this.mouseY);
        }
        this.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(this.tileMap == null)
            return;
        this.mouseX = e.getX()/16;
        this.mouseY = e.getY()/16;

        if(this.mouseX < 0 || this.mouseX >= this.tileMap.getWidth())
            this.mouseX = -1;
        if(this.mouseY < 0 || this.mouseY >= this.tileMap.getHeight())
            this.mouseY = -1;

        if(this.mouseX == -1 || this.mouseY == -1)
            return;

        if(this.mode == Mode.EVENTS && e.getClickCount() == 2) {
            EventEditDialog evEdit;
            if(this.tileMap.getEntityAt(mouseX, mouseY) == null) {
                evEdit = new EventEditDialog(new Entity(mouseX, mouseY));
            } else {
                Entity ent = this.tileMap.getEntityAt(mouseX, mouseY);
                evEdit = new EventEditDialog(ent);
            }
            evEdit.setVisible(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(this.tileMap == null)
            return;
        this.mouseX = e.getX()/16;
        this.mouseY = e.getY()/16;

        if(this.mouseX < 0 || this.mouseX >= this.tileMap.getWidth())
            this.mouseX = -1;
        if(this.mouseY < 0 || this.mouseY >= this.tileMap.getHeight())
            this.mouseY = -1;

        if(this.mouseX == -1 || this.mouseY == -1)
            return;

        if(this.mode == Mode.PAINT) {
            this.brush.onBrushBegin(this.tileMap, this.tileSetViewer.getSelectedTile(), this.mouseX, this.mouseY);
        } else if(this.mode == Mode.COLLISIONS) {
            this.tileMap.setCollisionAt(!e.isShiftDown(), this.mouseX, this.mouseY);
        }
        this.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(this.tileMap == null)
            return;
        this.mouseX = e.getX()/16;
        this.mouseY = e.getY()/16;

        if(this.mouseX < 0 || this.mouseX >= this.tileMap.getWidth())
            this.mouseX = -1;
        if(this.mouseY < 0 || this.mouseY >= this.tileMap.getHeight())
            this.mouseY = -1;

        if(this.mouseX == -1 || this.mouseY == -1)
            return;

        if(this.mode == Mode.PAINT) {
            this.brush.onBrushEnd(this.tileMap, this.tileSetViewer.getSelectedTile(), this.mouseX, this.mouseY);
        }
        this.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.mouseX = -1;
        this.mouseY = -1;
        this.repaint();
    }

    @Override
    public void onBrushChange(Brush b) {
        this.brush = b;
    }

    @Override
    public void onModeChange(Mode mode) {
        this.mode = mode;
        this.repaint();
    }

}
