package com.ninjaphase.pokered.editor.data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * <p>
 *     A {@code TileMap} is a map which can be edited.
 * </p>
 */
public class TileMap {

    private int width, height;
    private int[] tiles;

    private BufferedImage image;
    private BufferedImage[] tileImages;

    /**
     * <p>
     *     Constructs a new {@code TileMap}.
     * </p>
     *
     * @param width The width of the map.
     * @param height The height of the map.
     */
    public TileMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new int[width*height];
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
        this.tiles[0] = 1;
    }

    public void render(Graphics g) {
        for(int y = 0; y < this.getHeight(); y++) {
            for(int x = 0; x < this.getWidth(); x++) {
                g.drawImage(tileImages[this.getTileAt(x, y)], x*16, y*16, null);
            }
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getTileAt(int x, int y) {
        return this.tiles[x+(y*width)];
    }

    /**
     * @param img The image to convert.
     * @return The image.
     */
    public static BufferedImage toBufferedImage(Image img) {
        if(img instanceof BufferedImage)
            return (BufferedImage)img;

        BufferedImage image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return image;
    }

    public BufferedImage[] getTileset() {
        return this.tileImages;
    }

}
