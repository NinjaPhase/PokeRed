package com.ninjaphase.pokered.editor.data.story.tilemap;

import com.ninjaphase.pokered.editor.data.story.ResourceType;
import com.ninjaphase.pokered.editor.data.story.Story;
import com.ninjaphase.pokered.editor.util.Constants;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>
 *     The {@code TileSet} handles data for a bunch of tiles.
 * </p>
 */
public class TileSet {

    int id;
    String name;
    String tilesetImage;
    int[] tileData;

    Story story;

    /**
     * <p>
     *     Constructs a new {@code TileSet}.
     * </p>
     *
     * @param s The story this tileset belongs to.
     * @param id The id of the tileset.
     */
    public TileSet(Story s, int id) {
        this.story = s;
        this.id = id;
        this.name = "";
        this.tileData = new int[this.getImageColumns()*this.getImageRows()];
    }

    /**
     * <p>
     *     Constructs a new {@code TileSet} from existing data.
     * </p>
     *
     * @param s The story this tileset belongs to.
     * @param obj The JSONObject of the tileset.
     */
    public TileSet(Story s, JSONObject obj) {
        this(s, obj.optInt("id"));
        this.name = obj.optString("name");
        this.tilesetImage = obj.optString("graphic");
        this.tilesetImage = this.tilesetImage.substring(
                this.tilesetImage.lastIndexOf('/')+1, this.tilesetImage.lastIndexOf('.'));
    }

    /**
     * <p>
     *     Draws a tile on a graphics context at a given x and y.
     * </p>
     *
     * @param gc The graphics context.
     * @param id The id.
     * @param x The x position.
     * @param y The y position.
     */
    public void drawTile(GraphicsContext gc, int id, float x, float y) {
        this.drawTile(gc, id, x, y, 1);
    }

    /**
     * <p>
     *     Draws a tile on a graphics context at a given x and y.
     * </p>
     *
     * @param gc The graphics context.
     * @param id The id.
     * @param x The x position.
     * @param y The y position.
     * @param scale The graphics scale.
     */
    public void drawTile(GraphicsContext gc, int id, float x, float y, int scale) {
        if(this.getImage() == null)
            return;
        int tX = id%this.getImageColumns();
        int tY = id/this.getImageColumns();
        gc.drawImage(this.getImage(),
                tX*Constants.TILE_SIZE, tY*Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE,
                x, y, Constants.TILE_SIZE*scale, Constants.TILE_SIZE*scale);
    }

    /**
     * @return The JSONObject created.
     */
    public JSONObject getTileSetAsJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", this.getName());
        jsonObject.put("internal_name", this.getInternalName());
        jsonObject.put("graphic", ResourceType.IMAGE_TILESET + "/" + this.getTilesetImage() + ".png");
        jsonObject.put("id", this.getId());
        return jsonObject;
    }

    /**
     * @return The id of the tileset.
     */
    public int getId() {
        return this.id;
    }

    /**
     * @param name Sets the name of the tileset.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The name of this {@code TileSet}.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return The internal name of this {@code TileSet}.
     */
    public String getInternalName() {
        return this.getId() + "_" + this.getName().replaceAll("[^A-Za-z0-9-_]]", "");
    }

    /**
     * @param tilesetImage Sets the image of the tileset.
     */
    public void setTilesetImage(String tilesetImage) {
        this.tilesetImage = tilesetImage;
    }

    /**
     * @return The tileset image string.
     */
    public String getTilesetImage() {
        return this.tilesetImage;
    }

    /**
     * @return The image.
     */
    public Image getImage() {
        return (Image) this.story.getResourceManager().getResource(ResourceType.IMAGE_TILESET, this.tilesetImage);
    }

    /**
     * @return The amount of columns in the image.
     */
    public int getImageColumns() {
        return this.getImage() == null ? 0 : (int)(this.getImage().getWidth() / Constants.TILE_SIZE);
    }

    /**
     * @return The amount of rows in the image.
     */
    public int getImageRows() {
        return this.getImage() == null ? 0 : (int)(this.getImage().getHeight() / Constants.TILE_SIZE);
    }

    @Override
    public String toString() {
        return this.name;
    }

}
