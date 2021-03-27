package de.undefinedhuman.projectcreate.crafting.recipe;

import de.undefinedhuman.projectcreate.gui.Gui;
import de.undefinedhuman.projectcreate.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.projectcreate.utils.Utils;

public class RecipeTypePanel extends Gui {

    public RecipeTypePanel() {
        super(GuiTemplate.HOTBAR);
        set(new PixelConstraint(0), new RelativeConstraint(1), Utils.getInventoryConstraint(GuiTemplate.HOTBAR, 7), Utils.getInventoryConstraint(GuiTemplate.HOTBAR, 1));
        setOffsetY(new RelativeOffset(-1));
    }

}
