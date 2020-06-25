package de.undefinedhuman.sandboxgame.gui.elements;

import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.event.ClickEvent;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.Constraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;

public abstract class MenuSlot extends Gui {

    public MenuSlot(String iconPreview, Constraint x, Constraint y) {
        super(GuiTemplate.SLOT);
        set(x, y, new PixelConstraint(Variables.SLOT_SIZE), new PixelConstraint(Variables.SLOT_SIZE));
        addChild(new Gui(iconPreview).set("r0.5", "r0.5", "p" + Variables.ITEM_SIZE, "p" + Variables.ITEM_SIZE).setCentered());
        addEvent(new ClickEvent() {
            @Override
            public void onClick() {
                MenuSlot.this.onClick();
            }
        });
    }

    public abstract void onClick();

}
