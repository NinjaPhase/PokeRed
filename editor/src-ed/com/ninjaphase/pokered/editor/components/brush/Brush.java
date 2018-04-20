package com.ninjaphase.pokered.editor.components.brush;

import com.ninjaphase.pokered.editor.data.story.tilemap.TileMap;

/**
 * <p>
 *     A {@code Brush} is used to paint onto the canvas.
 * </p>
 */
public interface Brush {
    public static final Brush PENCIL_BRUSH = new PencilBrush();

    /**
     * <p>
     *     Called when the brush has been pressed.
     * </p>
     *
     * @param map The map.
     * @param tileId The selected tile.
     * @param x The x position.
     * @param y The y position.
     */
    public void onBrushBegin(TileMap map, int tileId, int x, int y);

    /**
     * <p>
     *     Called when the brush has started to move.
     * </p>
     *
     * @param map The map.
     * @param tileId The tile id.
     * @param x The x position.
     * @param y The y position.
     */
    public void onBrushMove(TileMap map, int tileId, int x, int y);

    /**
     * <p>
     *     Called when the brush is stopping.
     * </p>
     *
     * @param map The map.
     * @param tileId The tile id.
     * @param x The x position.
     * @param y The y position.
     */
    public void onBrushEnd(TileMap map, int tileId, int x, int y);

}
