package de.undefinedhuman.projectcreate.engine.items;

import com.badlogic.gdx.graphics.Color;
import de.undefinedhuman.projectcreate.engine.utils.Colors;

public enum Rarity {

    COMMON("Common", Colors.WHITE),
    UNCOMMON("Uncommon", Colors.LIGHT_GREEN),
    RARE("Rare", Colors.LIGHT_BLUE),
    EPIC("Epic", Colors.PURPLE),
    LEGENDARY("Legendary", Colors.ORANGE);

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
