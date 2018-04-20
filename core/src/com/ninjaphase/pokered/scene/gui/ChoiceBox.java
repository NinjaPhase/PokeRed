package com.ninjaphase.pokered.scene.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * <p>
 *     The {@code ChoiceBox} is used to display a choice to the player and allow them to select
 *     a choice.
 * </p>
 */
public class ChoiceBox<T> {

    private NinePatch patch;
    private BitmapFont font;
    private TextureRegion caret;
    private T[] choices;
    private int currentIndex;

    /**
     * <p>
     *     Constructs a new {@code ChoiceBox}.
     * </p>
     *
     * @param patch The patch.
     * @param font The font.
     * @param caret The caret.
     * @param choices The choices.
     */
    public ChoiceBox(NinePatch patch, BitmapFont font, TextureRegion caret, T... choices) {
        this.patch = patch;
        this.font = font;
        this.caret = caret;
        this.choices = choices;
    }

    /**
     * <p>
     *     Renders the {@code SpriteBatch}.
     * </p>
     *
     * @param batch The batch.
     */
    public void render(SpriteBatch batch, float x, float y, float w, float h) {
        patch.draw(batch, x, y, w, h);
        float tY = y+16f;
        for(int i = 0; i < this.choices.length; i++) {
            this.font.draw(batch, this.choices[this.choices.length-i-1].toString(), x+16f, tY);
            tY += 16f;
        }

        batch.draw(this.caret, x+8f, y+8f+(16f*(this.choices.length-currentIndex-1)));
    }

    /**
     * <p>
     *     Sets the current index.
     * </p>
     *
     * @param currentIndex The current index.
     */
    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = Math.max(0, Math.min(currentIndex, this.choices.length-1));
    }

    /**
     * @return The current index.
     */
    public int getCurrentIndex() {
        return this.currentIndex;
    }

    /**
     * @return The currently selected value.
     */
    public T getCurrentValue() {
        return this.choices[this.currentIndex];
    }

}
