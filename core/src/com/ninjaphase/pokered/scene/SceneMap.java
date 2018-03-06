package com.ninjaphase.pokered.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ninjaphase.pokered.PokemonApplication;
import com.ninjaphase.pokered.data.TileMap;
import com.ninjaphase.pokered.entity.*;
import com.ninjaphase.pokered.entity.event.MapEvent;
import com.ninjaphase.pokered.entity.event.MapEventPlayer;
import com.ninjaphase.pokered.scene.gui.MessageBox;

/**
 * <p>
 *     The {@code SceneMap} handles the TileMap for the game.
 * </p>
 */
public class SceneMap extends Scene implements MapEventPlayer, HumanMoveFinishEvent {

    private boolean up, down, left, right;

    private OrthographicCamera camera, uiCamera;
    private Player player;

    private Texture texPlayer;
    private MessageBox box;
    private NinePatch patch;
    private BitmapFont font;
    private MapEvent mapEvent;

    /**
     * <p>
     *     Constructs a new {@code SceneMap}.
     * </p>
     *
     * @param app The application.
     */
    public SceneMap(PokemonApplication app) {
        super(app);
        app.bgColor.set(0.0f, 0.0f, 0.0f, 1.0f);
        this.texPlayer = new Texture("img/player.png");
        this.patch = new NinePatch(new Texture("img/windowskin.png"), 16, 16, 16, 16);
        this.player = new Player(this.texPlayer, app.getDataManager().getStartMap(), 3, 1);
        this.player.addMoveListener(this);
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.uiCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.uiCamera.position.set(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f, 0.0f);
        this.uiCamera.update();
        this.font = new BitmapFont(Gdx.files.internal("font/basic_font.fnt"));
        this.box = new MessageBox(this.patch, this.font, "JOSH is\n\nplaying the SNES!");
        this.box.setVisible(false);
    }

    @Override
    void update(float deltaTime) {
        this.box.update(deltaTime);

        if(this.mapEvent == null) {
            this.player.update(deltaTime);
        } else if(this.mapEvent.isFinished(this)) {
            if(this.mapEvent.getNextEvent() != null) {
                this.mapEvent = this.mapEvent.getNextEvent();
                this.mapEvent.begin(this);
            } else {
                this.mapEvent = null;
            }
        }

        this.camera.position.set(this.player.position, 0.0f);
        this.camera.update();
    }

    @Override
    void render(SpriteBatch batch) {
        batch.setProjectionMatrix(this.camera.combined);
        this.player.getMap().render(batch);
        this.player.render(batch);

        batch.setProjectionMatrix(this.uiCamera.combined);
        this.box.render(batch);
    }

    @Override
    public void dispose() {
        this.texPlayer.dispose();
        this.patch.getTexture().dispose();
        this.font.dispose();
    }

    @Override
    public void queueEvent(MapEvent event) {
        this.mapEvent = event;
        this.mapEvent.begin(this);
    }

    @Override
    public void warp(String internalName, int x, int y) {
        if(this.app.getDataManager().getMap(internalName) == null)
            throw new NullPointerException("The warp to map cannot be null.");
        this.player.setMap(this.app.getDataManager().getMap(internalName), x, y);
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.UP && mapEvent == null) {
            this.up = true;
            this.player.move(EntityDirection.UP);
            return true;
        } else if(keycode == Input.Keys.DOWN && mapEvent == null) {
            this.down = true;
            this.player.move(EntityDirection.DOWN);
            return true;
        } else if(keycode == Input.Keys.LEFT && mapEvent == null) {
            this.left = true;
            this.player.move(EntityDirection.LEFT);
            return true;
        } else if(keycode == Input.Keys.RIGHT && mapEvent == null) {
            this.right = true;
            this.player.move(EntityDirection.RIGHT);
            return true;
        } else if(keycode == Input.Keys.Z) {
            if(this.box.isVisible() && this.box.isFinished()) {
                this.box.setVisible(false);
            } else if(mapEvent == null && !this.player.isMoving()) {
                int x = this.player.getX() + (
                        this.player.getLookDirection() == EntityDirection.LEFT ? -1 :
                                this.player.getLookDirection() == EntityDirection.RIGHT ? 1 : 0);
                int y = this.player.getY() + (
                        this.player.getLookDirection() == EntityDirection.UP ? 1 :
                                this.player.getLookDirection() == EntityDirection.DOWN ? -1 : 0);
                EventEntity e = this.player.getMap().getEntityAt(EventEntity.class, x, y);
                if(e != null && e.getTrigger().equalsIgnoreCase("interact")) {
                    this.queueEvent(e.getFirstEvent());
                }
            }
        } else if(keycode == Input.Keys.B) {
            app.getSceneManager().push(new SceneBattle(app));
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.UP) {
            this.up = false;
        } else if(keycode == Input.Keys.DOWN) {
            this.down = false;
        } else if(keycode == Input.Keys.LEFT) {
            this.left = false;
        } else if(keycode == Input.Keys.RIGHT) {
            this.right = false;
        }
        if(keycode == Input.Keys.UP || keycode == Input.Keys.DOWN || keycode == Input.Keys.LEFT ||
                keycode == Input.Keys.RIGHT) {
            if(this.up) {
                this.player.move(EntityDirection.UP);
            } else if(this.down) {
                this.player.move(EntityDirection.DOWN);
            } else if(this.left) {
                this.player.move(EntityDirection.LEFT);
            } else if(this.right) {
                this.player.move(EntityDirection.RIGHT);
            } else {
                this.player.move(null);
            }
            return true;
        }
        return false;
    }

    @Override
    public MessageBox getMessageBox() {
        return this.box;
    }

    @Override
    public boolean onMoveStep(HumanEntity entity, int x, int y) {
        EventEntity ev = this.player.getMap().getEntityAt(EventEntity.class, x, y);
        if(ev != null && ev.getTrigger().equalsIgnoreCase("touch")) {
            this.queueEvent(ev.getFirstEvent());
            return true;
        }
        return false;
    }
}
