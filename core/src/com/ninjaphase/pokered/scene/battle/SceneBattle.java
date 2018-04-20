package com.ninjaphase.pokered.scene.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.ninjaphase.pokered.PokemonApplication;
import com.ninjaphase.pokered.data.pokemon.Pokemon;
import com.ninjaphase.pokered.entity.Player;
import com.ninjaphase.pokered.scene.Scene;
import com.ninjaphase.pokered.scene.battle.Battle;
import com.ninjaphase.pokered.scene.battle.event.BattleEvent;
import com.ninjaphase.pokered.scene.battle.event.BattleEventPlayer;
import com.ninjaphase.pokered.scene.gui.EnemyStatusBox;
import com.ninjaphase.pokered.scene.gui.MessageBox;
import com.ninjaphase.pokered.scene.gui.PlayerStatusBox;
import com.ninjaphase.pokered.util.Constants;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * <p>
 *     The {@code SceneBattle} handles the battleData scene within the game.
 * </p>
 */
public class SceneBattle extends Scene implements BattleEventPlayer {

    private Battle battleData;
    private Pokemon playerPokemon, opponentPokemon;

    private BitmapFont font;
    private NinePatch patch;
    private TextureAtlas battleTextures;
    private TextureRegion texCaret;

    private int battleBoxX, battleBoxY, moveSelectIndex;
    private int selectedOption;

    private MessageBox messageBox;

    private BattleEvent currentEvent;
    private final Queue<BattleEvent> eventQueue;

    private Player player;

    private PlayerStatusBox pStatusBox;
    private EnemyStatusBox eStatusBox;
    private BitmapFont battleFont;

    /**
     * <p>
     *     Constructs the {@code SceneBattle}.
     * </p>
     *
     * @param app The appplication.
     */
    public SceneBattle(PokemonApplication app, Player player, Pokemon opponentPokemon) {
        super(app);
        this.eventQueue = new ArrayDeque<>();
        this.selectedOption = -1;
        this.player = player;
        this.playerPokemon = player.getActivePokemon();
        this.opponentPokemon = opponentPokemon;
        this.battleFont = new BitmapFont(Gdx.files.internal("font/battle_font.fnt"));
        this.patch = new NinePatch(
                app.getResourceManager().get(Texture.class, Constants.DEFAULT_WINDOWSKIN), 16, 16, 16, 16);
        TextureAtlas texMisc = app.getResourceManager().get(TextureAtlas.class, Constants.DEFAULT_MISC);
        this.texCaret = texMisc.findRegion("caret");
        this.battleTextures = new TextureAtlas("img/system/battle.pack");
        this.pStatusBox = new PlayerStatusBox(playerPokemon, battleFont, battleTextures);
        this.eStatusBox = new EnemyStatusBox(opponentPokemon, battleFont, battleTextures);
        this.font = app.getResourceManager().get(BitmapFont.class, Constants.DEFAULT_FONT);
        this.messageBox = new MessageBox(this.patch, this.font);
        app.bgColor.set(Constants.WHITE);
        this.battleData = new Battle(this, this.playerPokemon, this.opponentPokemon);
        this.battleData.beginBattle();
    }

    @Override
    protected void update(float deltaTime) {
        if(this.currentEvent == null || this.currentEvent.isFinished()) {
            if(eventQueue.peek() == null) {
                this.currentEvent = null;

                if(this.battleData.getState() == Battle.STATE.WIN || this.battleData.getState() == Battle.STATE.RAN) {
                    this.app.getSceneManager().pop();
                    this.app.bgColor.set(Color.BLACK);
                } else if(this.battleData.getState() == Battle.STATE.CAUGHT) {
                    this.player.addPokemon(this.opponentPokemon);
                    this.app.getSceneManager().pop();
                    this.app.bgColor.set(Color.BLACK);
                } else if(this.battleData.getState() == Battle.STATE.LOSE) {
                    this.player.healParty();
                    this.player.setMap(
                            app.getDataManager().getCurrentStory().getStartMap(), 3, 1
                    );
                    this.app.getSceneManager().pop();
                    this.app.bgColor.set(Color.BLACK);
                }
            } else {
                this.currentEvent = eventQueue.poll();
                this.currentEvent.begin(this);
            }
        } else {
            this.currentEvent.update(deltaTime);
        }
    }

    @Override
    protected void render(SpriteBatch batch) {
        // Rendering
        batch.draw(this.opponentPokemon.getSpecies().getFrontTexture(),
                app.getDefaultCamera().viewportWidth-72.0f, app.getDefaultCamera().viewportHeight-72.0f);
        this.eStatusBox.render(batch, 20.0f, app.getDefaultCamera().viewportHeight-48.0f);

        batch.draw(this.playerPokemon.getSpecies().getBackTex(), 40.0f, 48.0f);
        this.pStatusBox.render(batch, app.getDefaultCamera().viewportWidth-114.0f, 48.0f);

        // Controls
        this.patch.draw(batch, 0, 0.0f, 160.0f, 48.0f);
        this.messageBox.render(batch);
        if(this.currentEvent == null || this.currentEvent.isFinished()) {
            if(this.selectedOption == -1) {
                this.drawBattleOptions(batch, 64.0f, 0.0f);
            } else if(this.selectedOption == 0) {
                this.drawMoveOptions(batch);
            }
        }
    }

    private void drawBattleOptions(SpriteBatch batch, float x, float y) {
        this.patch.draw(batch, x, y, 96.0f, 48.0f);
        batch.draw(this.texCaret, x+8f+(this.battleBoxX*48), (y+24.0f)-(this.battleBoxY*16f));
        this.font.draw(batch, "FIGHT", x+16f, y+32.0f);
        this.font.draw(batch, "ITEM", x+16f, y+16.0f);
        this.font.draw(batch, "POKEMON", x+64f, y+32.0f);
        this.font.draw(batch, "RUN", x+64f, y+16.0f);
    }

    private void drawMoveOptions(SpriteBatch batch) {
        this.patch.draw(batch, 160.0f - 128.0f, 0.0f, 128.0f, 48.0f);
        this.patch.draw(batch, 0.0f, 40.0f, 88.0f, 40.0f);
        String moves = "";
        for(int i = 0; i < 4; i++) {
            moves += (this.playerPokemon.getMove(i) == null ? "-" : this.playerPokemon.getMove(i).getName()) + "\n";
        }
        this.font.draw(batch, moves, 48.0f, 40.0f);
        batch.draw(this.texCaret, 40.0f, 32.0f-(this.moveSelectIndex*8f));
        this.font.draw(batch, "TYPE/\n " + this.playerPokemon.getMove(this.moveSelectIndex).getType(), 8.0f, 72.0f);
        this.font.draw(batch, "35/" + this.playerPokemon.getMove(this.moveSelectIndex).getTotalPP(), 40.0f, 56.0f);
    }

    @Override
    public void dispose() {
        this.battleFont.dispose();
        this.battleTextures.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(this.currentEvent == null || this.currentEvent.isFinished()) {
            if (this.selectedOption == -1) {
                if (keycode == Input.Keys.DOWN) {
                    this.battleBoxY = Math.min(1, this.battleBoxY + 1);
                } else if (keycode == Input.Keys.UP) {
                    this.battleBoxY = Math.max(0, this.battleBoxY - 1);
                } else if (keycode == Input.Keys.LEFT) {
                    this.battleBoxX = Math.max(0, this.battleBoxX - 1);
                } else if (keycode == Input.Keys.RIGHT) {
                    this.battleBoxX = Math.min(1, this.battleBoxX + 1);
                } else if (keycode == Input.Keys.Z) {
                    if (this.battleBoxX == 0 && this.battleBoxY == 0) {
                        this.selectedOption = 0;
                    } else if(this.battleBoxX == 1 && this.battleBoxY == 1) {
                        this.battleData.attemptRun();
                    } else if(this.battleBoxX == 0 && this.battleBoxY == 1) {
                        this.battleData.attemptCatch(2.0f);
                    }
                }
            } else if (this.selectedOption == 0) {
                if (keycode == Input.Keys.DOWN) {
                    this.moveSelectIndex = Math.min(this.playerPokemon.getMoveCount() - 1, this.moveSelectIndex + 1);
                } else if (keycode == Input.Keys.UP) {
                    this.moveSelectIndex = Math.max(0, this.moveSelectIndex - 1);
                } else if (keycode == Input.Keys.Z) {
                    this.battleData.progress(this.moveSelectIndex);
                    this.selectedOption = -1;
                } else if (keycode == Input.Keys.X) {
                    this.selectedOption = -1;
                }
            }
        } else {
            return this.currentEvent.keyDown(keycode);
        }
        return false;
    }

    @Override
    public void queueEvent(BattleEvent ev) {
        this.eventQueue.add(ev);
    }

    @Override
    public MessageBox getMessageBox() {
        return this.messageBox;
    }

    @Override
    public PlayerStatusBox getPlayerStatus() {
        return this.pStatusBox;
    }

    @Override
    public EnemyStatusBox getEnemyStatus() {
        return this.eStatusBox;
    }
}
