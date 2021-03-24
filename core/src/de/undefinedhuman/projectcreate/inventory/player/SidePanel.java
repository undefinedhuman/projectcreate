package de.undefinedhuman.projectcreate.inventory.player;

import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.gui.Gui;
import de.undefinedhuman.projectcreate.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.gui.transforms.offset.PixelOffset;
import de.undefinedhuman.projectcreate.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.inventory.slot.MenuSlot;
import de.undefinedhuman.projectcreate.utils.Tools;

public class SidePanel extends Gui {

    public static SidePanel instance;

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

    public SidePanel() {
        super(GuiTemplate.SMALL_PANEL);
        if(instance == null) instance = this;

        set(new RelativeConstraint(1), new CenterConstraint(), Tools.getInventoryConstraint(GuiTemplate.SMALL_PANEL, 1), Tools.getInventoryConstraint(GuiTemplate.SMALL_PANEL, textures.length)).setOffset(new PixelOffset(-Tools.getInventorySize(GuiTemplate.SMALL_PANEL, 1) - 25), new CenterOffset());
        for (int i = textures.length-1; i >= 0; i--)
            addMenuItem(i, textures.length - i - 1);
    }

    private void addMenuItem(int i, int index) {
        addChild(new MenuSlot(new PixelConstraint(0), new PixelConstraint((Variables.SLOT_SIZE + Variables.SLOT_SPACE) * i), textures[index], () -> InventoryManager.instance.handleClick(index)));
    }

}
