package com.ninjaphase.pokered.scene.battle;

import com.ninjaphase.pokered.data.pokemon.Move;
import com.ninjaphase.pokered.data.pokemon.Pokemon;
import com.ninjaphase.pokered.data.pokemon.Stat;
import com.ninjaphase.pokered.entity.Player;
import com.ninjaphase.pokered.scene.battle.event.BattleEventPlayer;
import com.ninjaphase.pokered.scene.battle.event.ExperienceEvent;
import com.ninjaphase.pokered.scene.battle.event.HideTextBattleEvent;
import com.ninjaphase.pokered.scene.battle.event.StatusBattleEvent;
import com.ninjaphase.pokered.scene.battle.event.TextBattleEvent;

/**
 * <p>
 *     The {@code Battle} class handles a battle between two opponents.
 * </p>
 */
public class Battle {

    enum STATE {
        READY_TO_PROGRESS,
        SELECT_NEW_POKEMON,
        RAN,
        WIN,
        LOSE,
        CAUGHT
    }

    public enum BATTLE_PARTY {
        PLAYER,
        OPPONENT
    }

    private STATE state;

    private Player player;
    private Pokemon playerPokemon;
    private Pokemon opponentPokemon;

    private BattleEventPlayer eventPlayer;
    private int escapeCount;

    /**
     * <p>
     *     Constructs a new {@code Battle} between the playerPokemon and a wild pokemon.
     * </p>
     *
     * @param eventPlayer The battle event playerPokemon.
     * @param player The playerPokemon.
     * @param opponent The wild pokemon.
     */
    public Battle(BattleEventPlayer eventPlayer, Pokemon player, Pokemon opponent) {
        this.eventPlayer = eventPlayer;
        this.playerPokemon = player;
        this.opponentPokemon = opponent;
        this.state = STATE.READY_TO_PROGRESS;
    }

    /**
     * <p>
     *     Starts the battle.
     * </p>
     */
    void beginBattle() {
        this.escapeCount = 0;
        this.playerPokemon.resetTempData();
        this.opponentPokemon.resetTempData();
        this.eventPlayer.queueEvent(new TextBattleEvent("Wild " + opponentPokemon.getName() + "\nappeared!"));
        this.eventPlayer.queueEvent(new TextBattleEvent("Go! " + playerPokemon.getName() + "!"));
        this.eventPlayer.queueEvent(new HideTextBattleEvent());
    }

    /**
     * <p>
     *     Progresses the game.
     * </p>
     *
     * @param input The move input.
     */
    void progress(int input) {
        if(state != STATE.READY_TO_PROGRESS) {
            return;
        }
        boolean isFirst;
        if (this.playerPokemon.getStat(Stat.STAT_SPEED) > this.opponentPokemon.getStat(Stat.STAT_SPEED)) {
            isFirst = true;
        } else if (this.playerPokemon.getStat(Stat.STAT_SPEED) < this.opponentPokemon.getStat(Stat.STAT_SPEED)) {
            isFirst = false;
        } else {
            isFirst = Math.random() < 0.5f;
        }
        int opponentMove = (int)(this.opponentPokemon.getMoveCount()*Math.random());
        if(isFirst) {
            playTurn(BATTLE_PARTY.PLAYER, input);
            if(state == STATE.READY_TO_PROGRESS) {
                playTurn(BATTLE_PARTY.OPPONENT, opponentMove);
            }
            this.eventPlayer.queueEvent(new HideTextBattleEvent());
        } else {
            playTurn(BATTLE_PARTY.OPPONENT, opponentMove);
            if(state == STATE.READY_TO_PROGRESS) {
                playTurn(BATTLE_PARTY.PLAYER, input);
            }
            this.eventPlayer.queueEvent(new HideTextBattleEvent());
        }
    }

    /**
     * <p>
     *     Plays a current users turn, albeit the playerPokemon or opponentPokemon.
     * </p>
     *
     * @param input The move input.
     */
    private void playTurn(BATTLE_PARTY party, int input) {
        Pokemon pokeUser = null;
        Pokemon pokeTarget = null;
        if(party == BATTLE_PARTY.PLAYER) {
            pokeUser = this.playerPokemon;
            pokeTarget = this.opponentPokemon;
        } else {
            pokeUser = this.opponentPokemon;
            pokeTarget = this.playerPokemon;
        }

        Move move = pokeUser.getMove(input);

        if(party == BATTLE_PARTY.PLAYER) {
            this.eventPlayer.queueEvent(new TextBattleEvent(pokeUser.getName() + "\nused " + move.getName() + "!"));
        } else {
            this.eventPlayer.queueEvent(new TextBattleEvent("Enemy " + pokeUser.getName() + "\nused " + move.getName() + "!"));
        }

        move.useMove(this.eventPlayer, pokeUser, pokeTarget);

        this.eventPlayer.queueEvent(new StatusBattleEvent(
                party == BATTLE_PARTY.PLAYER ? BATTLE_PARTY.OPPONENT : BATTLE_PARTY.PLAYER, pokeTarget.getHealth()));

        if(playerPokemon.getHealth() == 0) {
            this.eventPlayer.queueEvent(new TextBattleEvent(this.playerPokemon.getName() + "\nfainted!"));
            this.eventPlayer.queueEvent(new TextBattleEvent("PLAYER is out of\nusable POKeMON!"));
            this.eventPlayer.queueEvent(new TextBattleEvent("PLAYER blacked\nout!"));
            this.state = STATE.LOSE;
        } else if(opponentPokemon.getHealth() == 0) {
            int expGain = this.playerPokemon.getExperienceGain(this.opponentPokemon);

            this.eventPlayer.queueEvent(new TextBattleEvent("Enemy " + this.opponentPokemon.getName() + "\nfainted!"));
            this.eventPlayer.queueEvent(new TextBattleEvent(this.playerPokemon.getName() + " gained\n" + expGain + " EXP. Points!"));

            int oldLevel = this.playerPokemon.getLevel();
            int levelGain = this.playerPokemon.addExperience(expGain);
            this.eventPlayer.queueEvent(new ExperienceEvent(this.playerPokemon, oldLevel));

            while(levelGain > 0) {
                this.eventPlayer.queueEvent(new ExperienceEvent(this.playerPokemon, oldLevel+levelGain));
                this.eventPlayer.queueEvent(new TextBattleEvent(this.playerPokemon.getName() + " grew\nto level " + this.playerPokemon.getLevel() + "!"));
                this.playerPokemon.recalculateStats();
                levelGain--;
            }
            this.state = STATE.WIN;
        }
    }

    /**
     * <p>
     *     Attempts to run from the battle.
     * </p>
     */
    void attemptRun() {
        float f = ((float)this.playerPokemon.getEffectiveStat(Stat.STAT_SPEED) * 32f) /
                ((float)this.opponentPokemon.getEffectiveStat(Stat.STAT_SPEED) / 4f) + 30f * this.escapeCount++;
        if(f >= (int)(Math.random() * 255f) || f >= 255f) {
            this.eventPlayer.queueEvent(new TextBattleEvent("Got away safely!"));
            this.state = STATE.RAN;
        } else {
            this.eventPlayer.queueEvent(new TextBattleEvent("Can't escape!"));
            this.playTurn(BATTLE_PARTY.OPPONENT, (int)(this.opponentPokemon.getMoveCount()*Math.random()));
            this.eventPlayer.queueEvent(new HideTextBattleEvent());
        }
    }

    /**
     * <p>
     *     Attempts to catch a pokemon.
     * </p>
     *
     * @param catchRate The catch rate.
     */
    public void attemptCatch(float catchRate) {
        boolean caught;
        int shakes = 0;
        if(catchRate >= 255f) {
            caught = true;
        } else {
            int N = 0;
            if(catchRate == 1f) {
                N = (int)(Math.random()*255f);
            } else if(catchRate == 1.5f) {
                N = (int)(Math.random()*200f);
            } else if(catchRate == 2f) {
                N = (int)(Math.random()*150f);
            }
            if(N > opponentPokemon.getSpecies().getCatchRate()) {
                caught = false;
            }
            int M = (int)(Math.random()*255f);
            float f = (float)Math.floor((float)opponentPokemon.getStat(Stat.STAT_HP) * 255f * 4f)/((float)opponentPokemon.getHealth() * 12f);
            if(f >= M) {
                caught = true;
            } else {
                caught = false;
            }
        }
        if(caught) {
            this.eventPlayer.queueEvent(new TextBattleEvent("All right!\n" + this.opponentPokemon.getName() + " was"));
            this.eventPlayer.queueEvent(new TextBattleEvent("caught!"));
            if(false) {
                this.eventPlayer.queueEvent(new TextBattleEvent("New POKeDEX data\nwill be added for"));
                this.eventPlayer.queueEvent(new TextBattleEvent(this.opponentPokemon.getName()));
            }
            if(false) {
                // TODO: Implement this
                this.eventPlayer.queueEvent(new TextBattleEvent("Do you want to\ngive a nickname"));
                this.eventPlayer.queueEvent(new TextBattleEvent("to " + this.opponentPokemon.getName() + "?"));
            }
            this.state = STATE.CAUGHT;
        } else {
            this.eventPlayer.queueEvent(new TextBattleEvent("Aww! It appeared\nto be caught!"));
            this.playTurn(BATTLE_PARTY.OPPONENT, (int)(this.opponentPokemon.getMoveCount()*Math.random()));
        }
    }

    /**
     * @return The state of the battle.
     */
    STATE getState() {
        return this.state;
    }

}
