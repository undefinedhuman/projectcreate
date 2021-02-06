package de.undefinedhuman.sandboxgame.crafting.recipe;

import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class RecipeTypePanel extends Gui {

    public RecipeTypePanel() {
        super(GuiTemplate.HOTBAR);
        set(new PixelConstraint(0), new RelativeConstraint(1), Tools.getInventoryConstraint(GuiTemplate.HOTBAR, 7), Tools.getInventoryConstraint(GuiTemplate.HOTBAR, 1));
        setOffsetY(new RelativeOffset(-1));
    }

}
