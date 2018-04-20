package com.ninjaphase.pokered.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.ninjaphase.pokered.PokemonApplication;

import java.util.ArrayList;

/**
 * <p>The {@code OnscreenControls} determines the different controls that can
 * be used on a touch screen device.</p>
 */
public class OnscreenControls implements InputProcessor, Disposable {
    private static final float ALPHA = 0.3f;

    private OrthographicCamera camera;
    private ArrayList<Control> controls;
    private TextureAtlas controlTextures;
    private boolean isVisible;

    /**
     * <p>Constructor for {@code OnscreenControls}.</p>
     */
    public OnscreenControls() {
        this.camera = PokemonApplication.createCamera(false, 640);
        this.camera.update();
        this.controlTextures = new TextureAtlas("img/system/input_buttons.pack");
        this.controls = new ArrayList<>();
        this.controls.add(new ButtonControl(this.controlTextures.findRegion("a_button"), new Vector2(32f, 32f), Input.Keys.Z));
        this.controls.add(new ButtonControl(this.controlTextures.findRegion("b_button"),new Vector2(100f, 32f), Input.Keys.X));
        TextureRegion startTexture = this.controlTextures.findRegion("start_button"),
                selectTexture = this.controlTextures.findRegion("select_button");
        this.controls.add(new ButtonControl(startTexture,
                new Vector2((camera.viewportWidth/2f)-startTexture.getRegionWidth()-4f, 32f), Input.Keys.ENTER));
        this.controls.add(new ButtonControl(selectTexture,
                new Vector2((camera.viewportWidth/2f)+4f, 32f), Input.Keys.BACKSPACE));
        this.controls.add(new DPADControl(this.controlTextures.findRegion("dpad"), new Vector2(camera.viewportWidth-156f, 32f)));
        this.isVisible = true;
    }

    public void render(SpriteBatch batch) {
        if(!isVisible)
            return;
        batch.setColor(1f, 1f, 1f, ALPHA);
        batch.setProjectionMatrix(camera.combined);
        for(Control c : controls) {
            c.render(batch);
        }
        batch.setColor(1f, 1f, 1f, 1f);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 v = camera.unproject(new Vector3(screenX, screenY, 0f));
        for(Control c : controls)
            if(c.onTouchDown(v.x, v.y, pointer))
                return true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 v = camera.unproject(new Vector3(screenX, screenY, 0f));
        for(Control c : controls)
            if(c.onTouchUp(v.x, v.y, pointer))
                return true;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 v = camera.unproject(new Vector3(screenX, screenY, 0f));
        for(Control c : controls)
            if(c.onTouchDragged(v.x, v.y, pointer))
                return true;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void dispose() {
        this.controlTextures.dispose();
        for(Control c : controls) {
            c.dispose();
        }
    }
}
