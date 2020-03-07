package de.undefinedhuman.sandboxgame.inventory.player;

import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.elements.MenuSlot;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.inventory.InventoryManager;
import de.undefinedhuman.sandboxgame.utils.Variables;

public class SidePanel extends Gui {

    private Gui[] buttons = new Gui[10];
    private String[] textures = new String[] {"gui/Inventory Icon.png", "gui/Chestplate-Preview.png", "Unknown.png", "Unknown.png", "Unknown.png", "Unknown.png", "Unknown.png", "Unknown.png", "Unknown.png", "Unknown.png"};

    public SidePanel() {

        super(GuiTemplate.SMALL_PANEL);
        set("r1", "r0.5", Variables.getInventoryWidth(GuiTemplate.SMALL_PANEL, 1), Variables.getInventoryHeight(GuiTemplate.SMALL_PANEL, 10)).setOffsetX("p-25").setCenteredX(-1).setCenteredY();

        for (int i = 0; i < buttons.length; i++) {

            int k = buttons.length - i - 1;
            addChild(new MenuSlot(textures[k], getCornerSize(), getTemplate().cornerSize + (Variables.SLOT_SIZE + Variables.SLOT_SPACE) * i) {
                @Override
                public void onClick() { InventoryManager.instance.handleClick(k); }
            });

        }

    }

}
