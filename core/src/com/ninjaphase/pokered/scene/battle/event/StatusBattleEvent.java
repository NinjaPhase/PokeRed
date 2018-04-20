package com.ninjaphase.pokered.scene.battle.event;

import com.ninjaphase.pokered.scene.battle.Battle;

public class StatusBattleEvent extends BattleEvent {
    private static final float SPEED = 2.0f;

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
                eventPlayer.getPlayerStatus().getHealth() : eventPlayer.getEnemyStatus().getHealth();
    }

    @Override
    public void update(float deltaTime) {
        t = Math.min(1.0f, t + (deltaTime * SPEED));
        int inter = oldValue + (int)((newValue-oldValue)*t);
        if(this.party == Battle.BATTLE_PARTY.PLAYER) player.getPlayerStatus().setHealth(inter);
        else if (this.party == Battle.BATTLE_PARTY.OPPONENT) player.getEnemyStatus().setHealth(inter);
    }

    @Override
    public boolean isFinished() {
        return t >= 1.0f;
    }
}
