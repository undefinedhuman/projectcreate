package de.undefinedhuman.sandboxgame.crafting.gui;

import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.RelativeOffset;

public class ScrollBar extends Gui {

    private Gui thumb;

    public ScrollBar() {
        super(GuiTemplate.SCROLL_BAR);
        thumb = (Gui) new Gui("gui/thumb.png")
                .setSize(new PixelConstraint(6), new PixelConstraint(18))
                .setPosition(new CenterConstraint(), new RelativeConstraint(1))
                .setOffset(new CenterOffset(), new RelativeOffset(-1));
        addChild(thumb);
    }
}
