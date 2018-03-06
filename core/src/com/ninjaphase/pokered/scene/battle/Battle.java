package com.ninjaphase.pokered.scene.battle;

import com.ninjaphase.pokered.data.Move;
import com.ninjaphase.pokered.data.Pokemon;
import com.ninjaphase.pokered.scene.battle.event.BattleEventPlayer;
import com.ninjaphase.pokered.scene.battle.event.HideTextBattleEvent;
import com.ninjaphase.pokered.scene.battle.event.StatusBattleEvent;
import com.ninjaphase.pokered.scene.battle.event.TextBattleEvent;

/**
 * <p>
 *     The {@code Battle} class handles a battle between two opponents.
 * </p>
 */
public class Battle {

    public enum STATE {
        READY_TO_PROGRESS,
        SELECT_NEW_POKEMON,
        RAN,
        WIN,
        LOSE
    }

    public enum BATTLE_PARTY {
        PLAYER,
        OPPONENT
    }

    private STATE state;

    private Pokemon player;
    private Pokemon opponent;

    private BattleEventPlayer eventPlayer;

    /**
     * <p>
     *     Constructs a new {@code Battle} between the player and a wild pokemon.
     * </p>
     *
     * @param eventPlayer The battle event player.
     * @param player The player.
     * @param opponent The wild pokemon.
     */
    public Battle(BattleEventPlayer eventPlayer, Pokemon player, Pokemon opponent) {
        this.eventPlayer = eventPlayer;
        this.player = player;
        this.opponent = opponent;
        this.state = STATE.READY_TO_PROGRESS;
    }

    /**
     * <p>
     *     Starts the battle.
     * </p>
     */
    public void beginBattle() {
        this.player.resetTempData();
        this.opponent.resetTempData();
        this.eventPlayer.queueEvent(new TextBattleEvent("Wild " + opponent.getName() + "\nappeared!"));
        this.eventPlayer.queueEvent(new TextBattleEvent("Go! " + player.getName() + "!"));
        this.eventPlayer.queueEvent(new HideTextBattleEvent());
    }

    /**
     * <p>
     *     Progresses the game.
     * </p>
     *
     * @param input The move input.
     */
    public void progress(int input) {
        if(state != STATE.READY_TO_PROGRESS) {
            return;
        }
        boolean isFirst = false;
        if (this.player.getStat(3) > this.opponent.getStat(3)) {
            isFirst = true;
        } else if (this.player.getStat(3) < this.opponent.getStat(3)) {
            isFirst = false;
        } else {
            isFirst = Math.random() < 0.5f;
        }
        int opponentMove = (int)(this.opponent.getMoveCount()*Math.random());
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
     *     Plays a current users turn, albeit the player or opponent.
     * </p>
     *
     * @param input The move input.
     */
    private void playTurn(BATTLE_PARTY party, int input) {
        Pokemon pokeUser = null;
        Pokemon pokeTarget = null;
        if(party == BATTLE_PARTY.PLAYER) {
            pokeUser = this.player;
            pokeTarget = this.opponent;
        } else {
            pokeUser = this.opponent;
            pokeTarget = this.player;
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

        if(player.getHealth() == 0) {
            this.state = STATE.LOSE;
        } else if(opponent.getHealth() == 0) {
            this.eventPlayer.queueEvent(new TextBattleEvent("Enemy " + this.opponent.getName() + "\nfainted!"));
            this.eventPlayer.queueEvent(new TextBattleEvent(this.player.getName() + " gained\n?? EXP. Points!"));

            if(false) {
                this.eventPlayer.queueEvent(new TextBattleEvent(this.player.getName() + " grew\nto level " + this.player.getLevel() + "!"));
            }
            this.state = STATE.WIN;
        }
    }

    /**
     * @return The state of the battle.
     */
    public STATE getState() {
        return this.state;
    }

}
