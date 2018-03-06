package com.ninjaphase.pokered.scene.battle.event;

import com.ninjaphase.pokered.scene.battle.Battle;

public class StatusBattleEvent extends BattleEvent {
    private static float SPEED = 2.0f;

    private float t = 0.0f;
    private int oldValue, newValue;

    private BattleEventPlayer player;
    private Battle.BATTLE_PARTY party;

    public StatusBattleEvent(Battle.BATTLE_PARTY party, int value) {
        this.party = party;
        this.newValue = value;
    }

    @Override
    public void begin(BattleEventPlayer eventPlayer) {
        this.player = eventPlayer;
        this.oldValue = this.party == Battle.BATTLE_PARTY.PLAYER ?
                eventPlayer.getPlayerStatusHealth() : eventPlayer.getOpponentStatusHealth();
    }

    @Override
    public void update(float deltaTime) {
        t += deltaTime * SPEED;
        int inter = oldValue + (int)((newValue-oldValue)*t);
        if(this.party == Battle.BATTLE_PARTY.PLAYER) player.setPlayerStatusHealth(inter);
        else if (this.party == Battle.BATTLE_PARTY.OPPONENT) player.setOpponentStatusHealth(inter);
    }

    @Override
    public boolean isFinished() {
        return t > 1.0f;
    }
}
