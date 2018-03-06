package com.ninjaphase.pokered.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ninjaphase.pokered.data.Pokemon;
import com.ninjaphase.pokered.data.TileMap;
import com.ninjaphase.pokered.data.Trainer;

/**
 * <p>
 *     The {@code Player} class handles the controls of the player when on the map and in-battle.
 * </p>
 */
public class Player extends HumanEntity implements Trainer {

    private int partyCount;
    private final Pokemon[] party;

    /**
     * <p>
     *     Constructs a new {@code Player}.
     * </p>
     *
     * @param texture The texture of the player.
     * @param x The x position.
     * @param y The y position.
     */
    public Player(Texture texture, TileMap map, int x, int y) {
        super(texture, map, x, y);
        this.party = new Pokemon[6];
    }

    /**
     * <p>
     *     Adds a pokemon to the active party.
     * </p>
     *
     * @param p The pokemon to add.
     * @return Whether the pokemon was added, due to full size or not.
     */
    public boolean addPokemon(Pokemon p) {
        if(this.isPartyFull())
            return false;
        for(int i = 0; i < this.party.length; i++) {
            if(this.party[i] != null)
                continue;
            this.party[i] = p;
            this.partyCount++;
            break;
        }
        return true;
    }

    /**
     * @return Whether the players party is full.
     */
    public boolean isPartyFull() {
        return this.partyCount >= 6;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Pokemon[] getParty() {
        return this.party;
    }

    @Override
    public int getPartyCount() {
        return this.partyCount;
    }
}
