package com.ninjaphase.pokered.scene;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.ninjaphase.pokered.PokemonApplication;
import com.ninjaphase.pokered.scene.battle.SceneBattle;
import com.ninjaphase.pokered.scene.gui.ChoiceBox;
import com.ninjaphase.pokered.util.Constants;
import com.ninjaphase.pokered.data.map.EncounterData;
import com.ninjaphase.pokered.data.pokemon.Pokemon;
import com.ninjaphase.pokered.data.map.TileMap;
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

    private MessageBox box;
    private NinePatch patch;
    private MapEvent mapEvent;
    private ChoiceBox<String> menuBox;
    private boolean inMenu;

    /**
     * <p>
     *     Constructs a new {@code SceneMap}.
     * </p>
     *
     * @param app The application.
     */
    SceneMap(PokemonApplication app) {
        super(app);
        app.bgColor.set(0.0f, 0.0f, 0.0f, 1.0f);
        this.patch = new NinePatch(
                app.getResourceManager().get(Texture.class, Constants.DEFAULT_WINDOWSKIN),
                16, 16, 16, 16);
        this.player = app.getDataManager().getCurrentStory().getPlayer();
        this.player.addPokemon(
                new Pokemon(app.getDataManager().getSpecies("Charmander"), 5));
        this.player.addMoveListener(this);
        this.camera = PokemonApplication.createCamera(false);
        this.uiCamera = PokemonApplication.createCamera(false);
        BitmapFont font = app.getResourceManager().get(BitmapFont.class, Constants.DEFAULT_FONT);
        this.box = new MessageBox(this.patch, font);
        this.box.setVisible(false);
        TextureAtlas texMisc = app.getResourceManager().get(TextureAtlas.class, Constants.DEFAULT_MISC);
        TextureRegion texCaret = texMisc.findRegion("caret");
        this.menuBox = new ChoiceBox<>(this.patch, font, texCaret,
                "POKeMON", "ITEM", "RED", "SAVE", "OPTION", "EXIT");
    }

    @Override
    protected void update(float deltaTime) {
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
    protected void render(SpriteBatch batch) {
        batch.setProjectionMatrix(this.camera.combined);
        this.player.getMap().render(batch);
        this.player.getMap().renderConnections(batch);
        this.player.render(batch);

        batch.setProjectionMatrix(this.uiCamera.combined);
        this.box.render(batch);
        if(this.inMenu) {
            this.menuBox.render(batch, PokemonApplication.V_WIDTH-80f, this.camera.viewportHeight-112f,
                    80f, 112f);
        }
    }

    @Override
    public void dispose() {
        this.patch.getTexture().dispose();
    }

    @Override
    public void queueEvent(MapEvent event) {
        this.mapEvent = event;
        this.mapEvent.begin(this);
    }

    @Override
    public void warp(String internalName, int x, int y) {
        if(this.app.getDataManager().getCurrentStory().getMap(internalName) == null)
            throw new NullPointerException("The warp to map cannot be null.");
        TileMap map = this.app.getDataManager().getCurrentStory().getMap(internalName);
        this.player.setMap(map, x, map.getHeight()-y-1);
    }

    @Override
    public boolean keyDown(int keycode) {
        if(this.inMenu) {
            if (keycode == Input.Keys.UP) {
                this.menuBox.setCurrentIndex(this.menuBox.getCurrentIndex()-1);
                return true;
            } else if(keycode == Input.Keys.DOWN) {
                this.menuBox.setCurrentIndex(this.menuBox.getCurrentIndex()+1);
                return true;
            } else if(keycode == Input.Keys.Z) {
                if(this.menuBox.getCurrentValue().equalsIgnoreCase("POKEMON")) {
                    this.app.getSceneManager().push(new ScenePartyList(this.app, this.player));
                } else if(this.menuBox.getCurrentValue().equalsIgnoreCase("ITEM")) {
                    this.app.getSceneManager().push(new SceneBag(this.app, this.player));
                } else if(this.menuBox.getCurrentValue().equalsIgnoreCase("EXIT")) {
                    this.inMenu = false;
                }
                return true;
            } else if(keycode == Input.Keys.X) {
                this.inMenu = false;
                return true;
            }
        } else {
            if (keycode == Input.Keys.UP && mapEvent == null) {
                this.up = true;
                this.player.move(EntityDirection.UP);
                return true;
            } else if (keycode == Input.Keys.DOWN && mapEvent == null) {
                this.down = true;
                this.player.move(EntityDirection.DOWN);
                return true;
            } else if (keycode == Input.Keys.LEFT && mapEvent == null) {
                this.left = true;
                this.player.move(EntityDirection.LEFT);
                return true;
            } else if (keycode == Input.Keys.RIGHT && mapEvent == null) {
                this.right = true;
                this.player.move(EntityDirection.RIGHT);
                return true;
            } else if (keycode == Input.Keys.Z) {
                if (this.box.isVisible() && this.box.isFinished()) {
                    this.box.setVisible(false);
                } else if (mapEvent == null && !this.player.isMoving()) {
                    int x = this.player.getX() + (
                            this.player.getLookDirection() == EntityDirection.LEFT ? -1 :
                                    this.player.getLookDirection() == EntityDirection.RIGHT ? 1 : 0);
                    int y = this.player.getY() + (
                            this.player.getLookDirection() == EntityDirection.UP ? 1 :
                                    this.player.getLookDirection() == EntityDirection.DOWN ? -1 : 0);
                    EventEntity e = this.player.getMap().getEntityAt(EventEntity.class, x, y);
                    if (e != null && e.getTrigger().equalsIgnoreCase("interact")) {
                        this.queueEvent(e.getFirstEvent());
                    }
                }
                return true;
            } else if(keycode == Input.Keys.ENTER && !this.player.isMoving()) {
                this.inMenu = true;
                return true;
            }
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
    public SceneManager getSceneManager() {
        return app.getSceneManager();
    }

    @Override
    public boolean onMoveStep(HumanEntity entity, int x, int y) {
        EventEntity ev = entity.getMap().getEntityAt(EventEntity.class, x, y);
        if(ev != null && ev.getTrigger().equalsIgnoreCase("touch")) {
            this.left = this.right = this.up = this.down = false;
            this.queueEvent(ev.getFirstEvent());
            return true;
        } else if(entity.getMap().isEncounterTile(x, y) && entity.getMap().getEncounterData() != null) {
            if(Math.random() < 0.2) {
                EncounterData.Encounter e = entity.getMap().getEncounterData().getEncounter();
                if(e == null)
                    return false;
                entity.move(null);
                this.left = this.right = this.up = this.down = false;
                this.app.getSceneManager().push(
                        new SceneBattle(this.app, this.player, new Pokemon(e.species, e.level)));
                return true;
            }
            return false;
        }
        return false;
    }
}
