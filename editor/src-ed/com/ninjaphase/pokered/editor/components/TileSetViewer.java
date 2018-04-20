package com.ninjaphase.pokered.editor.components;

import com.ninjaphase.pokered.editor.data.story.tilemap.TileMap;
import com.ninjaphase.pokered.editor.util.Constants;
import com.ninjaphase.pokered.editor.util.GraphicUtil;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

/**
 * <p>
 *     The {@code TileSetViewer} is a canvas which handles viewing and manipulating the selected tiles.
 * </p>
 */
public class TileSetViewer extends Canvas implements EventHandler<MouseEvent> {
    private static final Image IMAGE_TILEBACK = new Image(
            TileSetViewer.class.getResourceAsStream("/tile_background.png"));

    private TileMap map;
    private TileSelection tileSelection;

    private int startX, startY;

    /**
     * <p>
     *     Constructs a new {@code TileSetViewer}.
     * </p>
     */
    public TileSetViewer() {
        this.tileSelection = new TileSelection();
        this.setOnMousePressed(this);
        this.setOnMouseDragged(this);
    }

    /**
     * <p>
     *     Clears and redraws the selected tiles.
     * </p>
     */
    private void clearSelectedTiles() {
        for(int i = 0; i < this.tileSelection.tiles.length; i++)
            this.drawTile(this.getGraphicsContext2D(), this.tileSelection.tiles[i]);
    }

    /**
     * <p>
     *     Redraws the entire tileset.
     * </p>
     */
    private void drawTileSet() {
        if(map == null || map.getTileSet() == null || map.getTileSet().getImage() == null) {
            this.setWidth(0);
            this.setHeight(0);
        } else {
            this.setWidth(map.getTileSet().getImage().getWidth());
            this.setHeight(map.getTileSet().getImage().getHeight());

            GraphicsContext gc = this.getGraphicsContext2D();
            for(int y = 0; y < map.getTileSet().getImageRows(); y++) {
                for(int x = 0; x < map.getTileSet().getImageColumns(); x++) {
                    this.drawTile(gc, x+(y*map.getTileSet().getImageColumns()),
                            x* Constants.TILE_SIZE, y*Constants.TILE_SIZE);
                }
            }

            if(this.tileSelection != null) {
                int tX = this.tileSelection.tiles[0]%map.getTileSet().getImageColumns();
                int tY = this.tileSelection.tiles[0]/map.getTileSet().getImageColumns();

                GraphicUtil.drawWindow(gc, tX*Constants.TILE_SIZE, tY*Constants.TILE_SIZE,
                         this.tileSelection.w*Constants.TILE_SIZE, this.tileSelection.h*Constants.TILE_SIZE);
            }
        }
    }
    /**
     * <p>
     *     Draws the tile.
     * </p>
     *
     * @param gc The graphics context.
     * @param tileId The tile id.
     */
    private void drawTile(GraphicsContext gc, int tileId) {
        if(map == null || map.getTileSet() == null || map.getTileSet().getImage() == null)
            return;
        int x = tileId%map.getTileSet().getImageColumns();
        int y = tileId/map.getTileSet().getImageColumns();
        this.drawTile(gc, tileId, x*Constants.TILE_SIZE, y*Constants.TILE_SIZE);
    }

    /**
     * <p>
     *     Draws the tile at x and y.
     * </p>
     *
     * @param gc The graphics context.
     * @param tileId The tile id.
     * @param x The x coordinate.
     * @param y The y coordinate
     */
    private void drawTile(GraphicsContext gc, int tileId, float x, float y) {
        if(map == null || map.getTileSet() == null || map.getTileSet().getImage() == null)
            return;
        gc.drawImage(IMAGE_TILEBACK, x, y);
        map.getTileSet().drawTile(gc, tileId, x, y);
    }

    /**
     * <p>
     *     Sets the tile map.
     * </p>
     *
     * @param map The tile map.
     */
    public void setTileMap(TileMap map) {
        this.map = map;
        this.drawTileSet();
    }

    /**
     * @return The selected tile.
     */
    TileSelection getTileSelection() {
        return this.tileSelection;
    }

    @Override
    public void handle(MouseEvent e) {
        int tX = (int)(e.getX()/Constants.TILE_SIZE);
        int tY = (int)(e.getY()/Constants.TILE_SIZE);
        if(e.getEventType() == MouseEvent.MOUSE_PRESSED) {
            this.clearSelectedTiles();
            this.startX = tX;
            this.startY = tY;

            this.tileSelection.getTilesInSelection(this.map.getTileSet(), this.startX, this.startY, 1, 1);

            GraphicUtil.drawWindow(this.getGraphicsContext2D(),
                    tX * Constants.TILE_SIZE, tY * Constants.TILE_SIZE,
                    this.tileSelection.w*Constants.TILE_SIZE, this.tileSelection.h*Constants.TILE_SIZE);
        } else if(e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            this.clearSelectedTiles();
            int startX = Math.min(this.startX, tX);
            int startY = Math.min(this.startY, tY);
            int endX = Math.max(this.startX, tX);
            int endY = Math.max(this.startY, tY);

            this.tileSelection.getTilesInSelection(this.map.getTileSet(), startX, startY,
                    (endX-startX)+1, (endY-startY)+1);

            GraphicUtil.drawWindow(this.getGraphicsContext2D(),
                    startX * Constants.TILE_SIZE, startY * Constants.TILE_SIZE,
                    this.tileSelection.w*Constants.TILE_SIZE, this.tileSelection.h*Constants.TILE_SIZE);
        }
    }
}
