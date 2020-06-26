package de.undefinedhuman.sandboxgame.inventory.player;

import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.elements.MenuSlot;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.PixelOffset;
import de.undefinedhuman.sandboxgame.inventory.InventoryManager;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class SidePanel extends Gui {

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
        set(new RelativeConstraint(1), new CenterConstraint(), new PixelConstraint(Tools.getInventoryWidth(GuiTemplate.SMALL_PANEL, 1)), new PixelConstraint(Tools.getInventoryHeight(GuiTemplate.SMALL_PANEL, 10))).setOffset(new PixelOffset(-Tools.getInventoryWidth(GuiTemplate.SMALL_PANEL, 1)), new CenterOffset());

        Gui[] buttons = new Gui[10];
        for (int i = 0; i < buttons.length; i++) {

            int k = buttons.length - i - 1;
            addChild(new MenuSlot(textures[k], new PixelConstraint(getCornerSize()), new PixelConstraint(getTemplate().cornerSize + (Variables.SLOT_SIZE + Variables.SLOT_SPACE) * i)) {
                @Override
                public void onClick() { InventoryManager.instance.handleClick(k); }
            });

        }

    }

}
