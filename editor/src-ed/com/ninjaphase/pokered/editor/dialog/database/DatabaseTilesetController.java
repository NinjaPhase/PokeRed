package com.ninjaphase.pokered.editor.dialog.database;

import com.ninjaphase.pokered.editor.data.story.ResourceType;
import com.ninjaphase.pokered.editor.data.story.Story;
import com.ninjaphase.pokered.editor.data.story.tilemap.TileSet;
import com.ninjaphase.pokered.editor.util.Constants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * <p>
 *     The {@code DatabaseTilesetController} handles the manipulation of Tilesets within the application.
 * </p>
 *
 * @author NinjaPhase
 */
public class DatabaseTilesetController implements Initializable {
    private static final Image IMAGE_COLLISION =
            new Image(DatabaseTilesetController.class.getResourceAsStream("/tile_collision_full.png"));
    private static final Image IMAGE_COLLISION_HOVER =
            new Image(DatabaseTilesetController.class.getResourceAsStream("/tile_collision_full_hover.png"));
    private static final Image IMAGE_COLLISION_CLOSE =
            new Image(DatabaseTilesetController.class.getResourceAsStream("/tile_collision_full_close.png"));
    private static final Image IMAGE_COLLISION_CLOSE_HOVER =
            new Image(DatabaseTilesetController.class.getResourceAsStream("/tile_collision_full_close_hover.png"));
    private static final Image IMAGE_COLLISION_4 =
            new Image(DatabaseTilesetController.class.getResourceAsStream("/tile_collision_open.png"));
    private static final Image IMAGE_COLLISION_4_HOVER =
            new Image(DatabaseTilesetController.class.getResourceAsStream("/tile_collision_open_hover.png"));

    private enum Mode {
        MODE_COLLISION,
        MODE_FOURWAY_COLLISION,
        MODE_ENCOUNTER;
    }

    private Story story;
    private TileSetData currentlyEditing;

    @FXML private TextField txtName;
    @FXML private TextField txtResource;
    @FXML private ListView<TileSetData> tileSets;
    @FXML private Canvas cvsTileset;
    @FXML private ToggleGroup grpTilesetEdit;

    private int lastX, lastY;
    private Mode mode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.mode = Mode.MODE_COLLISION;
        this.grpTilesetEdit.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            if((newValue == null)) {
                Platform.runLater(() -> this.grpTilesetEdit.selectToggle(oldValue));
            }
        }));
    }

    /**
     * <p>
     *     Called when the canvas is moved on.
     * </p>
     *
     * @param me The mouse event.
     */
    @FXML public void onTileSetMouseMoved(MouseEvent me) {
        if(this.getTilesetImage() == null)
            return;
        int tX = (int)(me.getX()/Constants.TILE_SIZE);
        int tY = (int)(me.getY()/Constants.TILE_SIZE);
        int mX = (int)(me.getX() - tX*Constants.TILE_SIZE);
        int mY = (int)(me.getY() - tY*Constants.TILE_SIZE);

        if(lastX != tX || lastY != tY) {
            this.repaintTile(this.cvsTileset.getGraphicsContext2D(), this.lastX, this.lastY);
            this.lastX = tX;
            this.lastY = tY;
        }

        this.repaintTile(this.cvsTileset.getGraphicsContext2D(), tX, tY, mX, mY);
    }

    /**
     * <p>
     *     Called when the canvas is pressed on.
     * </p>
     *
     * @param me The mouse event.
     */
    @FXML public void onTileSetMousePressed(MouseEvent me) {
        if(this.getTilesetImage() == null)
            return;
        int tX = (int)(me.getX()/Constants.TILE_SIZE);
        int tY = (int)(me.getY()/Constants.TILE_SIZE);
        int mX = (int)(me.getX() - tX*Constants.TILE_SIZE);
        int mY = (int)(me.getY() - tY*Constants.TILE_SIZE);

        int cols = (int) (this.currentlyEditing.getTilesetImage(this.story).getWidth() / Constants.TILE_SIZE);
        this.currentlyEditing.setSolid(tX+(tY*cols), !this.currentlyEditing.isSolid(tX+(tY*cols)));

        if(lastX != tX || lastY != tY) {
            this.repaintTile(this.cvsTileset.getGraphicsContext2D(), this.lastX, this.lastY);
            this.lastX = tX;
            this.lastY = tY;
        }

        this.repaintTile(this.cvsTileset.getGraphicsContext2D(), tX, tY, mX, mY);
    }

    /**
     * <p>
     *     Paints the tileset onto the canvas.
     * </p>
     *
     * @param gc The context to draw onto.
     */
    private void paintTileset(GraphicsContext gc) {
        gc.clearRect(0, 0, this.cvsTileset.getWidth(), this.cvsTileset.getHeight());
        if(this.getTilesetImage() == null)
            return;
        Image img = this.getTilesetImage();
        gc.drawImage(img, 0, 0);
        for(int y = 0; y < img.getHeight()/Constants.TILE_SIZE; y++) {
            for(int x = 0; x < img.getWidth()/Constants.TILE_SIZE; x++) {
                this.paintData(gc, x, y, -1, -1);
            }
        }
    }
    /**
     * <p>
     *     Repaints a single tile.
     * </p>
     *
     * @param gc The graphics context.
     * @param x The tiles x coordinate.
     * @param y The tiles y coordinate.
     */
    private void repaintTile(GraphicsContext gc, int x, int y) {
        gc.drawImage(this.getTilesetImage(),
                x*Constants.TILE_SIZE, y*Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE,
                x*Constants.TILE_SIZE, y*Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);

        this.paintData(gc, x, y, -1, -1);
    }

    /**
     * <p>
     *     Repaints a single tile.
     * </p>
     *
     * @param gc The graphics context.
     * @param x The tiles x coordinate.
     * @param y The tiles y coordinate.
     * @param relX The mouse x relative to the tile.
     * @param relY The mouse y relative to the tile.
     */
    private void repaintTile(GraphicsContext gc, int x, int y, int relX, int relY) {
        gc.drawImage(this.getTilesetImage(),
                x*Constants.TILE_SIZE, y*Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE,
                x*Constants.TILE_SIZE, y*Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);

        this.paintData(gc, x, y, relX, relY);
    }

    /**
     * <p>
     *     Paints the data for the given tile.
     * </p>
     *
     * @param gc The graphics context.
     * @param x The tiles x.
     * @param y The tiles y.
     * @param relX The relative x of the mouse.
     * @param relY The relative y of the mouse.
     */
    private void paintData(GraphicsContext gc, int x, int y, int relX, int relY) {
        int tileId = x+(y*currentlyEditing.cols(this.story));
        if(this.mode == Mode.MODE_COLLISION) {
            if(relX != -1 && relY != -1) {
                gc.drawImage(
                        this.currentlyEditing.isSolid(tileId) ? IMAGE_COLLISION_CLOSE_HOVER : IMAGE_COLLISION_HOVER,
                        x*Constants.TILE_SIZE, y*Constants.TILE_SIZE);
            } else {
                gc.drawImage(this.currentlyEditing.isSolid(tileId) ? IMAGE_COLLISION_CLOSE : IMAGE_COLLISION,
                        x*Constants.TILE_SIZE, y*Constants.TILE_SIZE);
            }
        } else if(this.mode == Mode.MODE_FOURWAY_COLLISION) {
            // Top
            gc.drawImage(((relX > 10 && relX < 22) && (relY < 10)) ? IMAGE_COLLISION_4_HOVER : IMAGE_COLLISION_4,
                    0, 0, 32, 10,
                    x * Constants.TILE_SIZE, y * Constants.TILE_SIZE, 32, 10);

            // Bottom
            gc.drawImage(((relX > 10 && relX < 22) && (relY > 22)) ? IMAGE_COLLISION_4_HOVER : IMAGE_COLLISION_4,
                    0, 22, 32, 10,
                    x*Constants.TILE_SIZE, (y*Constants.TILE_SIZE)+22, 32, 10);

            // Left
            gc.drawImage(((relX < 10) && (relY > 10 && relY < 22)) ? IMAGE_COLLISION_4_HOVER : IMAGE_COLLISION_4,
                    0, 0, 10, 32,
                    x*Constants.TILE_SIZE, y*Constants.TILE_SIZE, 10, 32);

            // Right
            gc.drawImage(((relX > 22) && (relY > 10 && relY < 22)) ? IMAGE_COLLISION_4_HOVER : IMAGE_COLLISION_4,
                    22, 0, 10, 32,
                    (x*Constants.TILE_SIZE)+22, y*Constants.TILE_SIZE, 10, 32);
        }
    }

    @FXML
    public void onCollision() {
        this.mode = Mode.MODE_COLLISION;
        this.paintTileset(this.cvsTileset.getGraphicsContext2D());
    }

    @FXML
    public void onFourWay() {
        this.mode = Mode.MODE_FOURWAY_COLLISION;
        this.paintTileset(this.cvsTileset.getGraphicsContext2D());
    }

    @FXML
    public void onEncounter() {
        this.mode = Mode.MODE_ENCOUNTER;
        this.paintTileset(this.cvsTileset.getGraphicsContext2D());
    }

    /**
     * <p>
     *     Sets the currently selected tileset.
     * </p>
     *
     * @param ts The tileset.
     */
    private void setTileSet(TileSetData ts) {
        this.currentlyEditing = ts;
        this.txtName.setText(ts.name);
        this.txtResource.setText(ts.resource);
        this.cvsTileset.setWidth(this.getTilesetImage() == null ? 0.0 : this.getTilesetImage().getWidth());
        this.cvsTileset.setHeight(this.getTilesetImage() == null ? 0.0 : this.getTilesetImage().getHeight());
        this.paintTileset(this.cvsTileset.getGraphicsContext2D());
    }

    /**
     * <p>
     *     Sets the story of the tileset controller.
     * </p>
     *
     * @param story The story.
     */
    public void setStory(Story story) {
        this.story = story;
        for(TileSet ts : this.story.getDatabaseManager().getTileSets()) {
            this.tileSets.getItems().add(TileSetData.copy(story, ts));
        }
        this.tileSets.getSelectionModel().selectFirst();
        this.setTileSet(this.tileSets.getSelectionModel().getSelectedItem());
        this.txtName.textProperty().addListener((observable, oldValue, newValue) -> {
            this.currentlyEditing.name = newValue;
            this.tileSets.refresh();
        });
    }

    /**
     * @return The tileset image.
     */
    public Image getTilesetImage() {
        return this.currentlyEditing.getTilesetImage(this.story);
    }

    /**
     * <p>
     *     The {@code TileSetData} is the shell of a {@link TileSet} to allow for cancelling.
     * </p>
     */
    private static final class TileSetData {
        static final int TILE_SOLID_NORTH = 0x1, TILE_SOLID_SOUTH = 0x2,
                TILE_SOLID_WEST = 0x4, TILE_SOLID_EAST = 0x8, TILE_ENCOUNTER = 0x10;

        String name;
        String resource;
        int[] tileData;

        private boolean checkFlag(int flag, int tileId) {
            return (this.tileData[tileId] & flag) > 0;
        }

        void setSolid(int tileId, boolean value) {
            if(value) {
                this.tileData[tileId] |= (TILE_SOLID_NORTH | TILE_SOLID_SOUTH | TILE_SOLID_EAST | TILE_SOLID_WEST);
            } else {
                this.tileData[tileId] &= (~TILE_SOLID_NORTH & ~TILE_SOLID_SOUTH & ~TILE_SOLID_EAST & ~TILE_SOLID_WEST);
            }
        }

        public boolean isSolid(int tileId) {
            return this.isSolidNorth(tileId) || this.isSolidSouth(tileId) ||
                    this.isSolidWest(tileId) || this.isSolidEast(tileId);
        }

        boolean isSolidNorth(int tileId) {
            return this.checkFlag(TILE_SOLID_NORTH, tileId);
        }

        boolean isSolidSouth(int tileId) {
            return this.checkFlag(TILE_SOLID_SOUTH, tileId);
        }

        boolean isSolidWest(int tileId) {
            return this.checkFlag(TILE_SOLID_WEST, tileId);
        }

        boolean isSolidEast(int tileId) {
            return this.checkFlag(TILE_SOLID_EAST, tileId);
        }

        /**
         * @return The tileset image.
         */
        Image getTilesetImage(Story s) {
            return this.resource == null ? null :
                    (Image)s.getResourceManager().getResource(ResourceType.IMAGE_TILESET, this.resource);
        }

        int cols(Story s) {
            return this.getTilesetImage(s) == null ? 0 : (int)(this.getTilesetImage(s).getWidth() / Constants.TILE_SIZE);
        }

        int rows(Story s) {
            return this.getTilesetImage(s) == null ? 0 : (int)(this.getTilesetImage(s).getHeight() / Constants.TILE_SIZE);
        }

        /**
         * @return The name of the tileset.
         */
        public String toString() {
            return this.name;
        }

        /**
         * <p>
         *     Copies the tileset to be part of this data.
         * </p>
         *
         * @param set The tileset.
         * @return The tileset data.
         */
         static TileSetData copy(Story s, TileSet set) {
            TileSetData tsd = new TileSetData();
            tsd.name = set.getName();
            tsd.resource = set.getTilesetImage();
            Image img = tsd.getTilesetImage(s);
            if(img != null) {
                int cols = (int) (img.getWidth() / Constants.TILE_SIZE);
                int rows = (int) (img.getHeight() / Constants.TILE_SIZE);
                tsd.tileData = new int[cols*rows];
            } else {
                tsd.tileData = new int[0];
            }
            return tsd;
        }

    }

}
