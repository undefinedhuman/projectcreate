package de.undefinedhuman.sandboxgame.inventory.player;

import com.badlogic.gdx.graphics.Color;
import de.undefinedhuman.sandboxgame.engine.resources.font.Font;
import de.undefinedhuman.sandboxgame.inventory.Inventory;

public class PlayerInventory extends Inventory {

    public static PlayerInventory instance;

    public PlayerInventory() {
        super(10, 5);
        if(instance == null) instance = this;
        setTitle("Inventory", Font.Title, Color.WHITE);
        setVisible(false);
    }

}
