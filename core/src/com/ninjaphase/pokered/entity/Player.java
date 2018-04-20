package com.ninjaphase.pokered.entity;

import com.badlogic.gdx.graphics.Texture;
import com.ninjaphase.pokered.data.map.random.RandomRegion;
import com.ninjaphase.pokered.data.pokemon.Pokemon;
import com.ninjaphase.pokered.data.map.TileMap;
import com.ninjaphase.pokered.data.pokemon.Stat;
import com.ninjaphase.pokered.data.pokemon.Trainer;

/**
 * <p>
 *     The {@code Player} class handles the controls of the player when on the map and in-battle.
 * </p>
 */
public class Player extends HumanEntity implements Trainer {

    private int partyCount;
    private final Pokemon[] party;
    public final RandomRegion[] randomRegions;

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
        this.randomRegions = new RandomRegion[3];
        this.party = new Pokemon[6];
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(this.getY() >= this.map.getHeight() && this.map.hasConnection(EntityDirection.UP)) {
            this.setMap(this.map.getConnection(EntityDirection.UP),
                    tileX-map.getConnOffset(EntityDirection.UP),
                    0);
        } else if(this.getY() < 0 && this.map.hasConnection(EntityDirection.DOWN)) {
            this.setMap(this.map.getConnection(EntityDirection.DOWN),
                    tileX-map.getConnOffset(EntityDirection.DOWN),
                    this.map.getConnection(EntityDirection.DOWN).getHeight()-1);
        }
        if(this.getX() >= this.map.getWidth() && this.map.hasConnection(EntityDirection.RIGHT)) {
            this.setMap(this.map.getConnection(EntityDirection.RIGHT),
                    0, tileY-map.getConnOffset(EntityDirection.RIGHT));
        } else if(this.getX() < 0 && this.map.hasConnection(EntityDirection.LEFT)) {
            this.setMap(this.map.getConnection(EntityDirection.LEFT),
                    this.map.getConnection(EntityDirection.LEFT).getWidth()-1,
                    tileY-map.getConnOffset(EntityDirection.LEFT));
        }
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
     * <p>
     *     Switches the pokemon at A with the pokemon at B.
     * </p>
     * @param a The first pokemon.
     * @param b The second pokemon.
     */
    public void switchPokemon(int a, int b) {
        Pokemon pB = this.party[b];
        this.party[b] = this.party[a];
        this.party[a] = pB;
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

    @Override
    public void healParty() {
        for(Pokemon p : this.getParty()) {
            if(p == null)
                continue;
            p.setHealth(p.getStat(Stat.STAT_HP));
        }
    }

    @Override
    public Pokemon getActivePokemon() {
        for(Pokemon p : this.getParty()) {
            if(p == null || p.getHealth() == 0)
                continue;
            return p;
        }
        return null;
    }

    @Override
    boolean canMove(int x, int y) {
        if(y >= map.getHeight() && map.hasConnection(EntityDirection.UP)) {
            return !map.getConnection(EntityDirection.UP).getCollision(
                    x-map.getConnOffset(EntityDirection.UP), 0);
        } else if(y < 0 && map.hasConnection(EntityDirection.DOWN)) {
            return !map.getConnection(EntityDirection.DOWN).getCollision(
                    x-map.getConnOffset(EntityDirection.DOWN), map.getConnection(EntityDirection.DOWN).getHeight()-1);
        } else if(x >= map.getWidth() && map.hasConnection(EntityDirection.RIGHT)) {
            return !map.getConnection(EntityDirection.RIGHT).getCollision(
                    0, y-map.getConnOffset(EntityDirection.RIGHT)
            );
        } else if(x < 0 && map.hasConnection(EntityDirection.LEFT)) {
            return !map.getConnection(EntityDirection.LEFT).getCollision(
                    map.getConnection(EntityDirection.LEFT).getWidth()-1,
                    y-map.getConnOffset(EntityDirection.LEFT)
            );
        }
        return x >= 0 && x < this.map.getWidth() && y >= 0 && y < this.map.getHeight() && !this.map.getCollision(x, y);
    }
}
