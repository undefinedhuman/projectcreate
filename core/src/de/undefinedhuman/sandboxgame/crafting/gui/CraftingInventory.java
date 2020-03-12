package de.undefinedhuman.sandboxgame.crafting.gui;

import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.GuiManager;
import de.undefinedhuman.sandboxgame.gui.elements.MenuSlot;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;
import de.undefinedhuman.sandboxgame.engine.items.ItemType;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class CraftingInventory extends Gui {

    private ItemType[] itemTypes = new ItemType[] {ItemType.BLOCK, ItemType.WEAPON, ItemType.TOOL, ItemType.ARMOR, ItemType.STRUCTURE};

    public CraftingInventory() {
        super(GuiTemplate.SMALL_PANEL);
        set("r0.5", "r0.5", Tools.getInventoryWidth(GuiTemplate.SMALL_PANEL, 15), Tools.getInventoryHeight(GuiTemplate.SMALL_PANEL, 10)).setCentered();
        Gui menuBackground = createBackground("p" + getBaseCornerSize(), "r1", 1);
        menuBackground.setOffsetY("p" + (-getBaseCornerSize())).setCenteredY(-1);
        Gui recipeBackground = createBackground("p" + getBaseCornerSize(), "p" + getBaseCornerSize(), 8);
        addMenu(menuBackground, itemTypes);
        addRecipes(recipeBackground);
        addChild(menuBackground, recipeBackground);
        GuiManager.instance.addGui(this);
    }

    private Gui createBackground(String x, String y, int row) {
        Gui gui = new Gui(GuiTemplate.HOTBAR);
        gui.set(x, y, Tools.getInventoryWidth(Variables.HOTBAR_OFFSET, 7), Tools.getInventoryHeight(6, row));
        return gui;
    }

    private void addMenu(Gui background, ItemType... types) {
        for (int i = 0; i < types.length; i++)
            background.addChild(new MenuSlot(types[i].getPreviewTexture(), (Variables.HOTBAR_OFFSET + (Variables.SLOT_SIZE + Variables.SLOT_SPACE) * i), (int) (background.getBaseValue(Axis.HEIGHT) - Variables.HOTBAR_OFFSET - Variables.SLOT_SIZE)) {
                @Override
                public void onClick() {}
            });
    }

    private void addRecipes(Gui background) {
        for (int i = 0; i < 8; i++) {
            background.addChild(new Gui(GuiTemplate.SLOT)
                    .set("r0.5", "p" + (Variables.HOTBAR_OFFSET + (Variables.SLOT_SIZE + Variables.SLOT_SPACE) * i), "p" + (background.getBaseValue(Axis.WIDTH) - Variables.HOTBAR_OFFSET * 2), "p" + Variables.SLOT_SIZE)
                    .setCenteredX());
        }
        background.addChild(new Gui("gui/arrowdown.png").set("r0.5", "p0", "p25", "p25").setCentered());
    }

    private Gui addChild(int id) {
        return new Gui("Unknown.png");
    }

}
