package de.undefinedhuman.projectcreate.game.inventory.player;

import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.game.crafting.CraftingInventory;
import de.undefinedhuman.projectcreate.game.gui.Gui;
import de.undefinedhuman.projectcreate.game.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.game.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.game.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.game.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.game.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.game.gui.transforms.offset.PixelOffset;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.inventory.slot.MenuSlot;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class SidePanel extends Gui {

    private static volatile SidePanel instance;

    private String[] textures = new String[] {
            "gui/Inventory Icon.png",
            "gui/preview/equip/Chestplate-Preview.png",
            "gui/Locked-Icon.png",
            "gui/Locked-Icon.png",
            "gui/Locked-Icon.png",
            "gui/Locked-Icon.png",
            "gui/Locked-Icon.png",
            "gui/Locked-Icon.png",
            "gui/Locked-Icon.png",
            "gui/Locked-Icon.png"};

    private SidePanel() {
        super(GuiTemplate.SMALL_PANEL);
        set(new RelativeConstraint(1), new CenterConstraint(), Tools.getInventoryConstraint(GuiTemplate.SMALL_PANEL, 1), Tools.getInventoryConstraint(GuiTemplate.SMALL_PANEL, textures.length)).setOffset(new PixelOffset(-Tools.getInventorySize(GuiTemplate.SMALL_PANEL, 1) - 25), new CenterOffset());
        for (int i = textures.length-1; i >= 0; i--)
            addMenuItem(i, textures.length - i - 1);
    }

    private void addMenuItem(int i, int index) {
        addChild(new MenuSlot(new PixelConstraint(0), new PixelConstraint((Variables.SLOT_SIZE + Variables.SLOT_SPACE) * i), textures[index], () -> InventoryManager.getInstance().handleClick(index)));
    }

    public static SidePanel getInstance() {
        if (instance == null) {
            synchronized (SidePanel.class) {
                if (instance == null)
                    instance = new SidePanel();
            }
        }
        return instance;
    }

}
