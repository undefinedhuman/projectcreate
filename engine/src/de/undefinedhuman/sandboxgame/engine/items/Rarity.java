package de.undefinedhuman.sandboxgame.engine.items;

import com.badlogic.gdx.graphics.Color;

public enum Rarity {

    COMMON("Common", new Color(0.8f, 0.8f, 0.8f, 1f)),
    UNCOMMON("Uncommon", new Color(0.22f, 0.66f, 0.2f, 1f)),
    RARE("Rare", new Color(0.21f, 0.54f, 0.63f, 1f)),
    EPIC("Epic", new Color(0.44f, 0.21f, 0.63f, 1f)),
    LEGENDARY("Legendary", new Color(0.64f, 0.59f, 0.2f, 1f));

    private String name;
    private Color color;

    Rarity(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

}
