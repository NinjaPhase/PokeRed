package com.ninjaphase.pokered.scene.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * <p>
 *     The {@code MessageBox} is used to display a simple message at the bottom of the screen.
 * </p>
 */
public class MessageBox {
    private static final float MESSAGE_SPEED = 0.03f;

    private String msg, fullMessage;
    private NinePatch patch;
    private BitmapFont font;

    private int position;
    private float timer;
    private boolean visible;

    /**
     * <p>
     *     Constructs a new {@code MessageBox}.
     * </p>
     *
     * @param patch The ninepatch.
     * @param font The bitmap font.
     * @param message The message.
     */
    public MessageBox(NinePatch patch, BitmapFont font, String message) {
        this.patch = patch;
        this.font = font;
        this.setMessage("");
    }

    public void update(float deltaTime) {
        if(!this.visible)
            return;
        if(this.msg.length() < this.fullMessage.length()) {
            timer += deltaTime;
            while (timer >= MESSAGE_SPEED) {
                this.position++;
                this.msg = this.fullMessage.substring(0, this.position);
                timer -= MESSAGE_SPEED;
            }
        }
    }

    public void render(SpriteBatch batch) {
        if(this.visible) {
            this.patch.draw(batch, 0, 0, 160, 48);
            this.font.draw(batch, this.msg, 8.0f, 32.0f);
        }
    }

    public void setMessage(String message) {
        this.msg = "";
        this.fullMessage = message.replaceAll("\n", "\n\n");
        this.position = 0;
        this.timer = 0.0f;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public boolean isFinished() {
        return this.msg.length() == this.fullMessage.length();
    }

}
