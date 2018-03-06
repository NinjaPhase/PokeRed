package com.ninjaphase.pokered.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.ninjaphase.pokered.PokemonApplication;
import com.ninjaphase.pokered.data.Move;
import com.ninjaphase.pokered.data.Pokemon;
import com.ninjaphase.pokered.scene.battle.Battle;
import com.ninjaphase.pokered.scene.battle.event.BattleEvent;
import com.ninjaphase.pokered.scene.battle.event.BattleEventPlayer;
import com.ninjaphase.pokered.scene.battle.event.HideTextBattleEvent;
import com.ninjaphase.pokered.scene.battle.event.TextBattleEvent;
import com.ninjaphase.pokered.scene.gui.MessageBox;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * <p>
 *     The {@code SceneBattle} handles the battle scene within the game.
 * </p>
 */
public class SceneBattle extends Scene implements BattleEventPlayer {

    private Battle battle;
    private Pokemon player, enemy;

    private BitmapFont font;
    private NinePatch patch;
    private TextureAtlas battleTextures, texMisc;
    private TextureRegion texHealthFill, texCaret;

    private int battleBoxX, battleBoxY, moveSelectIndex;
    private int selectedOption;

    private MessageBox messageBox;

    private BattleEvent currentEvent;
    private final Queue<BattleEvent> eventQueue;

    private int playerHealth, enemyHealth;

    /**
     * <p>
     *     Constructs the {@code SceneBattle}.
     * </p>
     *
     * @param app The appplication.
     */
    public SceneBattle(PokemonApplication app) {
        super(app);
        this.eventQueue = new ArrayDeque<>();
        this.selectedOption = -1;
        this.player = new Pokemon(7, 5);
        this.enemy = new Pokemon(4, 5);
        this.playerHealth = this.player.getHealth();
        this.enemyHealth = this.enemy.getHealth();
        this.patch = new NinePatch(new Texture("img/windowskin.png"), 16, 16, 16, 16);
        this.texMisc = new TextureAtlas("img/misc.pack");
        this.texCaret = this.texMisc.findRegion("caret");
        this.battleTextures = new TextureAtlas("img/battle.pack");
        this.texHealthFill = this.battleTextures.findRegion("health_fill");
        this.font = new BitmapFont(Gdx.files.internal("font/basic_font.fnt"));
        this.messageBox = new MessageBox(this.patch, this.font, "");
        app.bgColor.set(1.0f, 1.0f, 1.0f, 1.0f);
        this.battle = new Battle(this, this.player, this.enemy);
        this.battle.beginBattle();
    }

    @Override
    void update(float deltaTime) {
        if(this.currentEvent == null || this.currentEvent.isFinished()) {
            if(eventQueue.peek() == null) {
                this.currentEvent = null;

                if(this.battle.getState() == Battle.STATE.WIN) {
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
    void render(SpriteBatch batch) {
        // Rendering
        batch.draw(this.battleTextures.findRegion("enemy_box"), 11.0f, 115.0f);
        float enemyPercentage = (float)this.enemyHealth / (float)this.enemy.getStat(0);
        batch.draw(this.texHealthFill, 32.0f, 123.0f, 48*enemyPercentage, 2);
        this.font.draw(batch, this.enemy.getName(), 8.0f, 144.0f);
        this.font.draw(batch, String.valueOf(this.enemy.getLevel()), 40.0f, 136.0f);
        batch.draw(this.enemy.getSpecies().getFrontTexture(), 92.0f, 90.0f);
        batch.draw(this.battleTextures.findRegion("player_box"), 72.0f, 51.0f);
        float playerPercentage = (float)this.playerHealth / (float)this.player.getStat(0);
        batch.draw(this.texHealthFill, 96.0f, 67.0f, 48*playerPercentage, 2);
        this.font.draw(batch, this.player.getName(), 80.0f, 88.0f);
        this.font.draw(batch, String.valueOf(this.player.getLevel()), 120.0f, 80.0f);
        batch.draw(this.player.getSpecies().getBackTex(), 4.0f, 48.0f);
        this.font.draw(batch, String.valueOf(this.playerHealth), 98.0f, 64.0f);
        this.font.draw(batch, String.valueOf(this.player.getStat(0)), 128.0f, 64.0f);

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
        this.font.draw(batch, "FIGHT", x+16f, y+32.0f);
        this.font.draw(batch, "RUN", x+64f, y+16.0f);
    }

    private void drawMoveOptions(SpriteBatch batch) {
        this.patch.draw(batch, 160.0f - 128.0f, 0.0f, 128.0f, 48.0f);
        this.patch.draw(batch, 0.0f, 40.0f, 88.0f, 40.0f);
        String moves = "";
        for(int i = 0; i < 4; i++) {
            moves += (this.player.getMove(i) == null ? "-" : this.player.getMove(i).getName()) + "\n";
        }
        this.font.draw(batch, moves, 48.0f, 40.0f);
        batch.draw(this.texCaret, 40.0f, 32.0f-(this.moveSelectIndex*8f));
        this.font.draw(batch, "TYPE/\n " + this.player.getMove(this.moveSelectIndex).getType(), 8.0f, 72.0f);
        this.font.draw(batch, "35/" + this.player.getMove(this.moveSelectIndex).getTotalPP(), 40.0f, 56.0f);
    }

    @Override
    public void dispose() {
        this.patch.getTexture().dispose();
        this.texMisc.dispose();
        this.battleTextures.dispose();
        this.font.dispose();
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
                    }
                }
            } else if (this.selectedOption == 0) {
                if (keycode == Input.Keys.DOWN) {
                    this.moveSelectIndex = Math.min(this.player.getMoveCount() - 1, this.moveSelectIndex + 1);
                } else if (keycode == Input.Keys.UP) {
                    this.moveSelectIndex = Math.max(0, this.moveSelectIndex - 1);
                } else if (keycode == Input.Keys.Z) {
                    this.battle.progress(this.moveSelectIndex);
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
    public void setPlayerStatusHealth(int i) {
        this.playerHealth = i;
    }

    @Override
    public int getPlayerStatusHealth() {
        return this.playerHealth;
    }

    @Override
    public void setOpponentStatusHealth(int i) {
        this.enemyHealth = i;
    }

    @Override
    public int getOpponentStatusHealth() {
        return this.enemyHealth;
    }
}
