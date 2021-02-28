package de.undefinedhuman.sandboxgame.gui.elements.scrollpanel;

import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.pool.GuiPool;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;

import java.util.function.Supplier;

public class PooledScrollPanel<T extends Gui> extends ScrollPanel<T> {

    private GuiPool<T> contentPool;

    public PooledScrollPanel(GuiTemplate template, Supplier<T> supplier) {
        super(template);
        contentPool = new GuiPool<>(supplier, 600000);
    }

    @Override
    public void removeGui(T gui) {
        contentPool.add(gui);
    }

    public T addContent() {
        T t = contentPool.get();
        addContent(t);
        return t;
    }

    @Override
    public void delete() {
        super.delete();
    }
}
