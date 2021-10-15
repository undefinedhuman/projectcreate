package de.undefinedhuman.projectcreate.game.inventory.player;

import com.badlogic.gdx.graphics.Color;
import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryType;
import de.undefinedhuman.projectcreate.core.inventory.Inventory;
import de.undefinedhuman.projectcreate.engine.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.engine.resources.font.Font;
import de.undefinedhuman.projectcreate.game.inventory.ClientInventory;

public class PlayerInventory extends ClientInventory<Inventory> {

    private static volatile PlayerInventory instance;

    private PlayerInventory() {
        super(GuiTemplate.SMALL_PANEL, InventoryType.INVENTORY, "Inventory");
        setTitle("Inventory", Font.TITLE, Color.WHITE);
        setVisible(false);
    }

    public static PlayerInventory getInstance() {
        if(instance != null)
            return instance;
        synchronized (PlayerInventory.class) {
            if (instance == null)
                instance = new PlayerInventory();
        }
        return instance;
    }

}
