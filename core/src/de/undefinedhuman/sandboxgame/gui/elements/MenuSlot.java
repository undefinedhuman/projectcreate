package de.undefinedhuman.sandboxgame.gui.elements;

import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.event.ClickEvent;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;

public abstract class MenuSlot extends Gui {

    public MenuSlot(String iconPreview, int x, int y) {
        super(GuiTemplate.SLOT);
        set("p" + x, "p" + y, "p" + Variables.SLOT_SIZE, "p" + Variables.SLOT_SIZE);
        Gui child = new Gui(iconPreview);
        child.set("r0.5", "r0.5", "p" + Variables.ITEM_SIZE, "p" + Variables.ITEM_SIZE).setCentered();
        addChild(child);
        addEvent(new ClickEvent() {
            @Override
            public void onClick() {
                MenuSlot.this.onClick();
            }
        });
    }

    public abstract void onClick();

}
