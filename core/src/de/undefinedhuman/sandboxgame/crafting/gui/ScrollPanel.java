package de.undefinedhuman.sandboxgame.crafting.gui;

import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;

public class ScrollPanel extends Gui {

    private Gui scrollBar;

    public ScrollPanel() {
        super(GuiTemplate.HOTBAR);
        scrollBar = new Gui(GuiTemplate.SCROLL_BAR);
        scrollBar.set(new RelativeConstraint(1), new PixelConstraint(0), new PixelConstraint(10), new RelativeConstraint(1));
        addChild(scrollBar);
    }

}
