package de.undefinedhuman.sandboxgame.inventory.player;

import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.PixelOffset;
import de.undefinedhuman.sandboxgame.inventory.InventoryManager;
import de.undefinedhuman.sandboxgame.inventory.slot.MenuSlot;
import de.undefinedhuman.sandboxgame.utils.Tools;

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
