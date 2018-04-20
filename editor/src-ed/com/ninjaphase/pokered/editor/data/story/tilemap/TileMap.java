package com.ninjaphase.pokered.editor.data.story.tilemap;

import com.ninjaphase.pokered.editor.components.MapNode;
import com.ninjaphase.pokered.editor.data.story.Story;
import com.ninjaphase.pokered.editor.util.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *     A {@code TileMap} is a map which can be edited.
 * </p>
 */
public class TileMap implements MapNode {
    private int id, tileSetId;
    private String name;
    private int width, height;
    private int[][] tiles;

    private TileMap parent;
    private int parentId;
    private ObservableList<TileMap> children;
    private ImageView treeIcon;

    private Story s;
    private JSONObject jsonObject;
    private boolean hasChanged;

    /**
     * <p>
     *     Constructs a new {@code TileMap}.
     * </p>
     *
     * @param name The name of the map.
     * @param width The width of the map.
     * @param height The height of the map.
     */
    public TileMap(Story s, int id, String name, int width, int height, int tileSetId) {
        this.s = s;
        this.id = id;
        this.tileSetId = tileSetId;
        this.treeIcon = new ImageView("/icons/icon_map.png");
        this.children = FXCollections.observableList(new LinkedList<>());
        this.children.addListener((ListChangeListener<TileMap>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (TileMap t : c.getAddedSubList()) {
                        t.parent = TileMap.this;
                    }
                } else if (c.wasRemoved()) {
                    for (TileMap t : c.getAddedSubList()) {
                        if (t.parent == this)
                            t.parent = null;
                    }
                }
            }
        });
        this.name = name;
        this.width = width;
        this.height = height;
        this.tiles = new int[Constants.LAYER_COUNT][width * height];
        Arrays.fill(this.tiles[0], 1);
        Arrays.fill(this.tiles[1], 0);
        Arrays.fill(this.tiles[2], 0);
    }

    /**
     * <p>
     *     Constructs a new {@code TileMap}.
     * </p>
     *
     * @param s The story.
     * @param jsonObject The json object.
     */
    public TileMap(Story s, JSONObject jsonObject) {
        this(s, jsonObject.optInt("id"), jsonObject.optString("name"),
                jsonObject.optInt("width"), jsonObject.optInt("height"), jsonObject.optInt("tileset_id"));
        this.parentId = jsonObject.has("parent") ? jsonObject.optInt("parent") : -1;
        for(int l = 0; l < this.tiles.length; l++) {
            for(int y = 0; y < this.height; y++) {
                for(int x = 0; x < this.width; x++) {
                    this.tiles[l][x+(y*width)] = jsonObject.optJSONArray("tiles")
                            .optJSONArray(l).optInt(x+(y*width));
                }
            }
        }
    }

    /**
     * <p>
     *     Draws the tile at x and y at the appropriate place.
     * </p>
     *
     * @param gc The graphics context.
     * @param layer The layer of the tile.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void drawTile(GraphicsContext gc, int layer, int x, int y) {
        this.drawTile(gc, layer, x, y, 1);
    }

    /**
     * <p>
     *     Draws the tile at x and y at the appropriate place.
     * </p>
     *
     * @param gc The graphics context.
     * @param layer The layer of the tile.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param scale The tile scale.
     */
    public void drawTile(GraphicsContext gc, int layer, int x, int y, int scale) {
        this.getTileSet().drawTile(gc, this.getTileAt(layer, x, y),
                x*Constants.TILE_SIZE*scale, y*Constants.TILE_SIZE*scale, scale);
    }

    /**
     * <p>
     *     Sets the tile at x and y to i.
     * </p>
     *
     * @param i The tile index.
     * @param layer The layer of the tile.
     * @param x The x position.
     * @param y The y position.
     */
    public void setTileAt(int i, int layer, int x, int y) {
        if(!this.isValidTile(layer, x, y))
            throw new ArrayIndexOutOfBoundsException("Invalid coordinate at (" + x + ", " + y + ")");
        this.hasChanged = true;
        this.tiles[layer][x+(y*width)] = i;
    }

    /**
     * @param layer The layer of the tile.
     * @param x The x position.
     * @param y The y position.
     * @return The tile id at x and y on the specified layer.
     */
    public int getTileAt(int layer, int x, int y) {
        if(!this.isValidTile(layer, x, y))
            throw new ArrayIndexOutOfBoundsException("Invalid coordinate at (" + x + ", " + y + ")");
        return this.tiles[layer][x+(y*width)];
    }

    /**
     * @param layer The layer of the tile.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return Whether the coordinate at x and y on layer is a valid tile.
     */
    public boolean isValidTile(int layer, int x, int y) {
        return layer >= 0 && layer < Constants.LAYER_COUNT && x >= 0 && x < this.width && y >= 0 && y < this.height;
    }

    /**
     * <p>
     *     Resizes the entire map.
     * </p>
     *
     * @param newWidth The new width of the map.
     * @param newHeight The new height of the map.
     */
    public void resize(int newWidth, int newHeight) {
        this.hasChanged = true;
        int[][] newTiles = new int[this.tiles.length][newWidth*newHeight];
        for(int l = 0; l < newTiles.length; l++) {
            for (int y = 0; y < Math.min(newHeight, this.height); y++) {
                System.arraycopy(this.tiles[l], y * width, newTiles[l],
                        y * newWidth, Math.min(newWidth, this.width));
            }
        }
        this.tiles = newTiles;
        this.width = newWidth;
        this.height = newHeight;
    }

    /**
     * <p>
     *     Gets the map as a JSON file.
     * </p>
     *
     * @return The json object.
     * @throws JSONException Thrown if there is a problem with loading the json.
     */
    public JSONObject getMapAsJSON() throws JSONException {
        if(this.jsonObject != null && !this.hasChanged) {
            return this.jsonObject;
        }
        this.jsonObject = new JSONObject();
        jsonObject.put("name", this.getName());
        jsonObject.put("internal_name", this.getIdName());
        jsonObject.put("id", this.getId());
        jsonObject.put("tileset", this.getTileSet().getInternalName());
        jsonObject.put("tileset_id", this.getTileSet().getId());
        jsonObject.put("width", this.getWidth());
        jsonObject.put("height", this.getHeight());
        JSONArray layers = new JSONArray();
        for(int i = 0; i < this.getLayerCount(); i++) {
            JSONArray layer = new JSONArray();
            for(int y = 0; y < this.getHeight(); y++) {
                for(int x = 0; x < this.getWidth(); x++) {
                    layer.put(this.getTileAt(i, x, y));
                }
            }
            layers.put(layer);
        }
        jsonObject.put("tiles", layers);
        jsonObject.put("parent", this.getParent() == null ? null : this.getParent().getId());
        return jsonObject;
    }

    /**
     * @return The id of the map.
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return The id of the tileset.
     */
    public int getTileSetId() {
        return this.tileSetId;
    }

    /**
     * @return The tileset of the TileMap.
     */
    public TileSet getTileSet() {
        return this.s.getDatabaseManager().getTileSet(this.getTileSetId());
    }

    /**
     * @return The name of the map.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return The internal name.
     */
    public String getIdName() {
        return this.id + "_" + this.name
                .replaceAll("[^A-Za-z0-9-_]", "");
    }

    /**
     * <p>
     *     Sets the name to something new.
     * </p>
     *
     * @param newValue The new name value.
     */
    public void setName(String newValue) {
        this.name = newValue;
    }

    /**
     * @return The width of the map.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * @return The height of the map.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * @return The layer count.
     */
    public int getLayerCount() {
        return this.tiles.length;
    }

    /**
     * @return The parent id.
     */
    public int getParentId() {
        return this.parentId;
    }

    /**
     * @return The parent tilemap for the tree view.
     */
    public TileMap getParent() {
        return this.parent;
    }

    /**
     * @return The list of children this tilemap has for the tree view.
     */
    public List<TileMap> getChildren() {
        return this.children;
    }

    @Override
    public ImageView getIcon() {
        return this.treeIcon;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
