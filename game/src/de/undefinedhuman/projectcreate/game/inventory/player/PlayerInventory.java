package de.undefinedhuman.projectcreate.game.inventory.player;

import com.badlogic.gdx.graphics.Color;
import de.undefinedhuman.projectcreate.engine.resources.font.Font;
import de.undefinedhuman.projectcreate.game.inventory.Inventory;

public class PlayerInventory extends Inventory {

    private static volatile PlayerInventory instance;

    // IMPLEMENT POOLING FOR SLOTS, CONNECT TO ONE INVENTORY (NETWORK FROM CORE MODULE), LISTEN TO SLOT UPDATES, AND RESIZE THIS GUI INVENTORY WHEN ASSIGNED TO NEW NETWORK INVENTORY AND USE SLOTS FROM POOL

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
