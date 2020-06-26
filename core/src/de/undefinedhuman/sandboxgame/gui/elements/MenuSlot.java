package de.undefinedhuman.sandboxgame.gui.elements;

import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.event.ClickEvent;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.Constraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.CenterOffset;

public abstract class MenuSlot extends Gui {

    public MenuSlot(String iconPreview, Constraint x, Constraint y) {
        super(GuiTemplate.SLOT);
        set(x, y, new PixelConstraint(Variables.SLOT_SIZE), new PixelConstraint(Variables.SLOT_SIZE));
        addChild(new Gui(iconPreview).set(new CenterConstraint(), new CenterConstraint(), new PixelConstraint(Variables.ITEM_SIZE), new PixelConstraint(Variables.ITEM_SIZE)).setOffset(new CenterOffset(), new CenterOffset()));
        addEvent(new ClickEvent() {
            @Override
            public void onClick() {
                MenuSlot.this.onClick();
            }
        });
    }

    public abstract void onClick();

}
