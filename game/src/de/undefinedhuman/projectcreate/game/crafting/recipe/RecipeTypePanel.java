package de.undefinedhuman.projectcreate.game.crafting.recipe;

import de.undefinedhuman.projectcreate.engine.gui.Gui;
import de.undefinedhuman.projectcreate.engine.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class RecipeTypePanel extends Gui {

    public RecipeTypePanel() {
        super(GuiTemplate.HOTBAR);
        set(new PixelConstraint(0), new RelativeConstraint(1), Tools.getInventoryConstraint(GuiTemplate.HOTBAR, 7), Tools.getInventoryConstraint(GuiTemplate.HOTBAR, 1));
        setOffsetY(new RelativeOffset(-1));
    }

}
