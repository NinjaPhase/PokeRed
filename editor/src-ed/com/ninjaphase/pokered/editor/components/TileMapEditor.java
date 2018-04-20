package com.ninjaphase.pokered.editor.components;

import com.ninjaphase.pokered.editor.data.story.tilemap.TileMap;
import com.ninjaphase.pokered.editor.util.Constants;
import com.ninjaphase.pokered.editor.util.GraphicUtil;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * <p>
 *     The {@code TileMapEditor} is the component in which the
 *     {@link com.ninjaphase.pokered.editor.data.story.tilemap.TileMap TileMap} will be edited.
 * </p>
 *
 * @author NinjaPhase
 */
public class TileMapEditor extends Canvas implements EventHandler<MouseEvent> {

    private TileMap map;
    private TileSetViewer tileSetViewer;

    private int layer;
    private int mouseX, mouseY;
    private int startX, startY;
    private TileSelection tileSelection;

    private int tileScale;

    /**
     * <p>
     *     Constructs a new {@code TileMapEditor}.
     * </p>
     */
    public TileMapEditor() {
        this.layer = 0;
        this.mouseX = this.mouseY = -1;
        this.setOnMousePressed(this);
        this.setOnMouseDragged(this);
        this.setOnMouseMoved(this);
        this.setOnMouseExited(this);
        this.tileScale = 1;
    }

    /**
     * <p>
     *     Initialises the {@code TileMapEditor} to use the {@code TileSetViewer}.
     * </p>
     */
    public void init(TileSetViewer tileSetViewer) {
        this.tileSetViewer = tileSetViewer;
        this.tileSelection = this.tileSetViewer.getTileSelection();
    }

    /**
     * <p>
     *     Renders the tile map.
     * </p>
     */
    private void renderTileMap(GraphicsContext gc) {
        if(this.map == null) {
            this.setWidth(0.0);
            this.setHeight(0.0);
        } else {
            this.setWidth(this.map.getWidth()* Constants.TILE_SIZE * this.tileScale);
            this.setHeight(this.map.getHeight() * Constants.TILE_SIZE * this.tileScale);

            for (int y = 0; y < this.map.getHeight(); y++) {
                for (int x = 0; x < this.map.getWidth(); x++) {
                    this.drawTile(gc, x, y);
                }
            }
        }
    }

    /**
     * <p>
     *     Clears and redraws the selected tiles.
     * </p>
     */
    private void clearSelectedTiles() {
        for(int y = 0; y < this.tileSelection.h; y++) {
            for(int x = 0; x < this.tileSelection.w; x++) {
                if(!this.map.isValidTile(this.layer, this.mouseX+x, this.mouseY+y))
                    continue;
                this.drawTile(this.getGraphicsContext2D(), this.mouseX+x, this.mouseY+y);
            }
        }
    }

    /**
     * <p>
     *     Sets the currently edited tile map.
     * </p>
     *
     * @param tileMap The tile map.
     */
    public void setTileMap(TileMap tileMap) {
        this.map = tileMap;
        this.renderTileMap(this.getGraphicsContext2D());
    }

    /**
     * <p>
     *     Sets the current layer of the tile map.
     * </p>
     *
     * @param layer The layer.
     */
    public void setLayer(int layer) {
        if(layer < 0 || layer >= this.map.getLayerCount())
            throw new ArrayIndexOutOfBoundsException("Layer " + layer + " is out of bounds.");
        this.layer = layer;
        this.renderTileMap(this.getGraphicsContext2D());
    }

    /**
     * <p>
     *     Updates the tile map.
     * </p>
     */
    public void updateTileMap() {
        this.renderTileMap(this.getGraphicsContext2D());
    }

    @Override
    public void handle(MouseEvent event) {
        GraphicsContext gc = this.getGraphicsContext2D();
        int tX = (int)(event.getX()/(Constants.TILE_SIZE*this.tileScale));
        int tY = (int)(event.getY()/(Constants.TILE_SIZE*this.tileScale));
        if(tX < 0 || tX >= this.map.getWidth() || tY < 0 || tY >= this.map.getHeight())
            return;
        if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            this.startX = this.mouseX;
            this.startY = this.mouseY;
        }

        if(event.getEventType() == MouseEvent.MOUSE_PRESSED || event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            if(this.mouseX != -1 && this.mouseY != -1) {
                this.clearSelectedTiles();
            }
            this.mouseX = tX;
            this.mouseY = tY;
            int xDiff = Math.max(this.mouseX, this.startX)-Math.min(this.mouseX, this.startX);
            int yDiff = Math.max(this.mouseY, this.startY)-Math.min(this.mouseY, this.startY);

            for(int y = 0; y < this.tileSelection.h; y++) {
                for(int x = 0; x < this.tileSelection.w; x++) {
                    if(!this.map.isValidTile(this.layer, this.mouseX+x, this.mouseY+y))
                        continue;
                    this.map.setTileAt(this.tileSelection.getTile(x+xDiff, y+yDiff),
                            this.layer, this.mouseX+x, this.mouseY+y);
                    this.drawTile(gc, this.mouseX+x, this.mouseY+y);
                }
            }

            GraphicUtil.drawWindow(gc, this.mouseX*Constants.TILE_SIZE*this.tileScale,this.mouseY*Constants.TILE_SIZE*this.tileScale,
                    this.tileSelection.w*Constants.TILE_SIZE*this.tileScale, this.tileSelection.h*Constants.TILE_SIZE*this.tileScale);
        } else if(event.getEventType() == MouseEvent.MOUSE_MOVED) {
            if(this.mouseX != -1 && this.mouseY != -1) {
                this.clearSelectedTiles();
            }
            this.mouseX = tX;
            this.mouseY = tY;
            this.drawTile(gc, this.mouseX, this.mouseY);
            GraphicUtil.drawWindow(gc, this.mouseX*Constants.TILE_SIZE*this.tileScale,this.mouseY*Constants.TILE_SIZE*this.tileScale,
                    this.tileSelection.w*Constants.TILE_SIZE*this.tileScale, this.tileSelection.h*Constants.TILE_SIZE*this.tileScale);
        } else if(event.getEventType() == MouseEvent.MOUSE_EXITED) {
            this.clearSelectedTiles();
            this.mouseX = this.mouseY = -1;
        }
    }

    /**
     * <p>
     *     Draws the tile at x and y.
     * </p>
     *
     * @param gc The graphics context.
     * @param x The x position.
     * @param y The y position.
     */
    private void drawTile(GraphicsContext gc, int x, int y) {
        gc.setFill(Color.BLACK);
        gc.fillRect(x*Constants.TILE_SIZE*this.tileScale, y*Constants.TILE_SIZE*this.tileScale,
                Constants.TILE_SIZE*this.tileScale, Constants.TILE_SIZE*this.tileScale);
        for(int l = 0; l < this.map.getLayerCount(); l++) {
            if(l == this.layer) gc.setGlobalAlpha(1);
            else gc.setGlobalAlpha(0.33);

            this.map.drawTile(gc, l, x, y, this.tileScale);
        }
        gc.setGlobalAlpha(1);
    }

}
