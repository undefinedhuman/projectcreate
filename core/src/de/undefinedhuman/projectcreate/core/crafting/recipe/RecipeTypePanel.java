package de.undefinedhuman.projectcreate.core.crafting.recipe;

import de.undefinedhuman.projectcreate.core.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.core.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.core.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.projectcreate.core.gui.Gui;
import de.undefinedhuman.projectcreate.core.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.core.utils.Tools;

public class RecipeTypePanel extends Gui {

    public RecipeTypePanel() {
        super(GuiTemplate.HOTBAR);
        set(new PixelConstraint(0), new RelativeConstraint(1), Tools.getInventoryConstraint(GuiTemplate.HOTBAR, 7), Tools.getInventoryConstraint(GuiTemplate.HOTBAR, 1));
        setOffsetY(new RelativeOffset(-1));
    }

}
