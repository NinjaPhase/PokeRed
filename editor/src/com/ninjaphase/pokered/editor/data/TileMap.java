package com.ninjaphase.pokered.editor.data;

import com.ninjaphase.pokered.editor.data.entity.Entity;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *     A {@code TileMap} is a map which can be edited.
 * </p>
 */
public class TileMap {

    private String name, internalName;
    private int width, height;
    private int[] tiles, collisions;
    private List<Entity> entities;

    private BufferedImage image;
    private BufferedImage[] tileImages;

    private File file;

    /**
     * <p>
     *     Constructs a new {@code TileMap}.
     * </p>
     *
     * @param name The name of the map.
     * @param width The width of the map.
     * @param height The height of the map.
     */
    public TileMap(String name, int width, int height) {
        this.entities = new LinkedList<>();
        this.name = name;
        this.internalName = this.name.toLowerCase().replaceAll(" ", ".")
                .replaceAll("[^A-Za-z0-9]", "");
        this.width = width;
        this.height = height;
        this.tiles = new int[width*height];
        this.collisions = new int[width*height];
        try {
            this.image = toBufferedImage(ImageIO.read(new File("./tiles.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int cols = this.image.getWidth()/16;
        int rows = this.image.getHeight()/16;

        this.tileImages = new BufferedImage[cols*rows];
        for(int y = 0; y < rows; y++) {
            for(int x = 0; x < cols; x++) {
                this.tileImages[x+(y*cols)] = this.image.getSubimage(x*16,y*16, 16, 16);
            }
        }
        Arrays.fill(this.tiles, 1);
    }

    /**
     * <p>
     *     Constructs a new {@code TileMap} from existing data.
     * </p>
     *
     * @param jsonObject The json object.
     */
    public TileMap(JSONObject jsonObject) {
        this(jsonObject.optString("name"), jsonObject.optInt("width"), jsonObject.optInt("height"));
        for(int i = 0; i < this.tiles.length; i++) {
            this.tiles[i] = jsonObject.optJSONArray("tiles").optInt(i);
        }
        for(int i = 0; i < this.collisions.length; i++) {
            this.collisions[i] = jsonObject.optJSONArray("collisions").optInt(i);
        }
        for(int i = 0; i < jsonObject.optJSONArray("entities").length(); i++) {
            this.entities.add(new Entity(jsonObject.optJSONArray("entities").optJSONObject(i)));
        }
    }

    /**
     * <p>
     *     Renders the map.
     * </p>
     *
     * @param g The graphics.
     */
    public void render(Graphics g) {
        for(int y = 0; y < this.getHeight(); y++) {
            for(int x = 0; x < this.getWidth(); x++) {
                g.drawImage(tileImages[this.getTileAt(x, y)], x*16, y*16, null);
            }
        }
    }

    /**
     * <p>
     *     Renders the collisions.
     * </p>
     *
     * @param g The graphics component.
     * @param c The color.
     */
    public void renderCollisions(Graphics g, Color c) {
        Color old = g.getColor();
        g.setColor(c);
        for(int y = 0; y < this.getHeight(); y++) {
            for(int x = 0; x < this.getWidth(); x++) {
                if(!this.isCollisionAt(x, y))
                    continue;
                g.fillRect(x*16, y*16, 16, 16);
            }
        }
        g.setColor(old);
    }

    /**
     * <p>
     *     Renders the entities.
     * </p>
     *
     * @param g The g object.
     */
    public void renderEntities(Graphics g) {
        for(Entity e : this.entities) {
            g.fillRect(e.getX()*16, e.getY()*16, 16, 16);
        }
    }

    /**
     * @return The {@code TileMap} as a JSON String for saving.
     */
    public String toJSONString() {
        StringBuilder str = new StringBuilder();
        str.append("{\n");
        str.append("  \"name\": \"Test\",\n");
        str.append("  \"internal_name\": \"test\",\n");
        str.append("  \"width\": "); str.append(this.width); str.append(",\n");
        str.append("  \"height\": "); str.append(this.height); str.append(",\n");
        TileMap.printIntArray(str, "tiles", this.tiles, this.width, this.height);
        str.append(",\n");
        TileMap.printIntArray(str, "collisions", this.collisions, this.width, this.height);
        str.append(",\n");
        str.append("  \"entities\": [\n");
        int i = 0;
        for(Entity e : this.entities) {
            str.append(e.toJSONString());
            if(i < this.entities.size()-1)
                str.append(",");
            str.append("\n");
            i++;
        }
        str.append("  ]");
        str.append("}");
        return str.toString();
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
     * <p>
     *     Sets the tile at x and y to i.
     * </p>
     *
     * @param i The tile index.
     * @param x The x position.
     * @param y The y position.
     */
    public void setTileAt(int i, int x, int y) {
        if(i < 0 || i >= this.tileImages.length)
            throw new ArrayIndexOutOfBoundsException("Unable to set tile to this value.");
        if(x < 0 || x >= this.width || y < 0 || y >= this.height)
            throw new ArrayIndexOutOfBoundsException("Map position is out of bounds.");
        this.tiles[x+(y*width)] = i;
    }

    /**
     * <p>
     *     Adds a collision to x and y.
     * </p>
     *
     * @param b Whether to collide.
     * @param x The x position.
     * @param y The y position.
     */
    public void setCollisionAt(boolean b, int x, int y) {
        this.collisions[x+(y*width)] = b ? 1 : 0;
    }

    /**
     * @param x The x position.
     * @param y The y position.
     * @return The tile index.
     */
    private int getTileAt(int x, int y) {
        return this.tiles[x+(y*width)];
    }
    public boolean isCollisionAt(int x, int y) {
        return this.collisions[x+(y*width)] == 1;
    }
    public Entity getEntityAt(int x, int y) {
        for(Entity e : this.entities) {
            if(e.getX() != x || e.getY() != y)
                continue;
            return e;
        }
        return null;
    }

    /**
     * @return Gets the tileset images.
     */
    public BufferedImage[] getTileset() {
        return this.tileImages;
    }

    /**
     * <p>
     *     Sets the file associated with the map.
     * </p>
     *
     * @param f The file.
     */
    public void setFile(File f) {
        this.file = f;
    }

    /**
     * @return The file associated with this {@code TileMap}.
     */
    public File getFile() {
        return this.file;
    }

    /**
     * @param img The image to convert.
     * @return The image.
     */
    private static BufferedImage toBufferedImage(Image img) {
        if(img instanceof BufferedImage)
            return (BufferedImage)img;

        BufferedImage image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return image;
    }

    /**
     * <p>
     *     Prints an array of ints in accordance to a width and height in JSON format.
     * </p>
     *
     * @param str The string.
     * @param name The name
     * @param array The array.
     * @param w The width.
     * @param h The height.
     */
    private static void printIntArray(StringBuilder str, String name, int[] array, int w, int h) {
        str.append("  \""); str.append(name); str.append("\": [\n    ");
        for(int y = 0; y < h; y++) {
            for(int x = 0; x < w; x++) {
                str.append(array[x+(y*w)]);
                if(x+(y*w) < array.length-1)
                    str.append(", ");
            }
            str.append("\n  ");
            if(y < h-1)
                str.append("  ");
        }
        str.append("]");
    }

}
