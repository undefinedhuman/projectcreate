package de.undefinedhuman.projectcreate.game.inventory.player;

import de.undefinedhuman.projectcreate.engine.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.game.inventory.ClientInventory;

public class PlayerInventory extends ClientInventory {

    private static volatile PlayerInventory instance;

    private PlayerInventory() {
        super(GuiTemplate.SMALL_PANEL);
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
