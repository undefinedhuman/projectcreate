package de.undefinedhuman.projectcreate.game.inventory.player;

import com.badlogic.gdx.graphics.Color;
import de.undefinedhuman.projectcreate.engine.resources.font.Font;
import de.undefinedhuman.projectcreate.game.inventory.Inventory;

public class PlayerInventory extends Inventory {

    private static volatile PlayerInventory instance;

    private PlayerInventory() {
        super(10, 5);
        setTitle("Inventory", Font.Title, Color.WHITE);
        setVisible(false);
    }

    public static PlayerInventory getInstance() {
        if (instance == null) {
            synchronized (PlayerInventory.class) {
                if (instance == null)
                    instance = new PlayerInventory();
            }
        }
        return instance;
    }

}
