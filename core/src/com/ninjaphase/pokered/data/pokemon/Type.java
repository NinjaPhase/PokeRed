package com.ninjaphase.pokered.data.pokemon;

/**
 * <p>
 *     The {@code Type} handles the type effectiveness of Pokémon.
 * </p>
 */
public enum Type {
    TYPE_NORMAL(0, "normal", null, new int[]{1}, null),
    TYPE_FIGHTING(1, "fighting", new int[]{5, 6, 16}, new int[]{2, 13, 17}, null),
    TYPE_FLYING(2, "flying", new int[]{1, 6, 11}, new int[]{5, 12, 14}, new int[]{4}),
    TYPE_POISON(3, "poison", new int[]{1, 3, 6, 11, 17}, new int[]{4, 13}, null),
    TYPE_GROUND(4, "ground", new int[]{3, 5}, new int[]{10, 11, 14}, new int[]{12}),
    TYPE_ROCK(5, "rock", new int[]{0, 2, 3, 9}, new int[]{1, 11, 4, 8, 10}, null),
    TYPE_BUG(6, "bug", new int[]{1, 4, 11}, new int[]{2, 5, 9}, null),
    TYPE_GHOST(7, "ghost", new int[]{3, 6}, new int[]{7, 16}, new int[]{0, 1}),
    TYPE_STEEL(8, "steel", new int[]{6, 15, 17, 2, 11, 14, 0, 13, 5, 8}, new int[]{1, 9, 4}, new int[]{3}),
    TYPE_FIRE(9, "fire", new int[]{6, 17, 9, 11, 14, 8}, new int[]{4, 5, 10}, null),
    TYPE_WATER(10, "water", new int[]{9, 14, 8, 10}, new int[]{12, 11}, null),
    TYPE_GRASS(11, "grass", new int[]{12, 11, 4, 10}, new int[]{6, 9, 2, 14, 3}, null),
    TYPE_ELECTRIC(12, "electric", new int[]{12, 1, 8}, new int[]{4}, null),
    TYPE_PSYCHIC(13, "psychic", new int[]{1, 13}, new int[]{6, 16, 7}, null),
    TYPE_ICE(14, "ice", new int[]{14}, new int[]{1, 9, 5, 8}, null),
    TYPE_DRAGON(15, "dragon", new int[]{12, 9, 11, 10}, new int[]{15, 17, 14}, null),
    TYPE_DARK(16, "dark", new int[]{16, 7}, new int[]{6, 17, 1}, new int[]{13}),
    TYPE_FAIRY(17, "fairy", new int[]{6, 16, 1}, new int[]{3, 8}, new int[]{15});

    private int id;
    private String name;
    private int[] resists, weak, immune;

    Type(int id, String name, int[] resists, int[] weak, int[] immune) {
        this.id = id;
        this.name = name;
        this.resists = resists;
        this.weak = weak;
        this.immune = immune;
    }

    public float getEffectiveness(Type attacker) {
        if(this.resists != null) {
            for (int i : this.resists) {
                if (i == attacker.id)
                    return 0.5f;
            }
        }
        if(this.weak != null) {
            for (int i : this.weak) {
                if (i == attacker.id)
                    return 2.0f;
            }
        }
        if(this.immune != null) {
            for (int i : this.immune) {
                if (i == attacker.id)
                    return 0.0f;
            }
        }
        return 1.0f;
    }


}
