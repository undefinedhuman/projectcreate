package de.undefinedhuman.sandboxgame.crafting.gui;

import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.event.ChangeEvent;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;

import java.util.ArrayList;

public class ScrollBar extends Gui {

    private Gui thumb;

    private ArrayList<ChangeEvent> listener = new ArrayList<>();

    public ScrollBar(GuiTemplate template) {
        super(template);
    }

    public ScrollBar addListener(ChangeEvent changeListener) {
        this.listener.add(changeListener);
        return this;
    }

}
