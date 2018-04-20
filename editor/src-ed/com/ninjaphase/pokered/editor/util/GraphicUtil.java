package com.ninjaphase.pokered.editor.util;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * <p>
 *     The {@code GraphicUtil} provides common canvas functions for the application.
 * </p>
 */
public final class GraphicUtil {
    private static final Image SELECTED_IMAGE = new Image(GraphicUtil.class.getResourceAsStream("/selected.png"));

    private GraphicUtil() {}

    /**
     * <p>
     *     Draws a window within a confined space with the default windowimage.
     * </p>
     *
     * @param gc The gc.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param w The width.
     * @param h The height.
     */
    public static void drawWindow(GraphicsContext gc, double x, double y, double w, double h) {
        GraphicUtil.drawWindow(gc, GraphicUtil.SELECTED_IMAGE, x, y, w, h);
    }

    /**
     * <p>
     *     Draws a window within a confined space.
     * </p>
     *
     * @param gc The gc.
     * @param windowImage The window image.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param w The width.
     * @param h The height.
     */
    public static void drawWindow(GraphicsContext gc, Image windowImage, double x, double y, double w, double h) {
        gc.drawImage(windowImage, 0, 0, 8, 8, x, y, 8, 8);
        int xCol = (int)((w-16)/8);
        int yCol = (int)((h-16)/8);
        gc.drawImage(windowImage, 8, 0, 8, 8,
                x+8, y, xCol*8, 8);
        gc.drawImage(windowImage, 16, 0, 8, 8,
                x+w-8, y, 8, 8);
        gc.drawImage(windowImage, 0, 16, 8, 8,
                x, y+h-8, 8, 8);
        gc.drawImage(windowImage, 16, 16, 8, 8,
                x+w-8, y+h-8, 8, 8);
        gc.drawImage(windowImage, 8, 16, 8, 8,
                x+8, y+h-8, xCol*8, 8);
        gc.drawImage(windowImage, 0, 8, 8, 8,
                x, y+8, 8, yCol*8);
        gc.drawImage(windowImage, 16, 8, 8, 8,
                x+w-8, y+8, 8, yCol*8);
    }

}
