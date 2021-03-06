package de.undefinedhuman.projectcreate.engine.gui.elements.scrollpanel;

import de.undefinedhuman.projectcreate.engine.utils.ds.ObjectPool;
import de.undefinedhuman.projectcreate.engine.utils.ds.Poolable;
import de.undefinedhuman.projectcreate.engine.gui.Gui;
import de.undefinedhuman.projectcreate.engine.gui.texture.GuiTemplate;

import java.util.function.Supplier;

public class PoolableScrollPanel<T extends Gui & Poolable> extends ScrollPanel<T> {

    private ObjectPool<T> contentPool;

    public PoolableScrollPanel(GuiTemplate template, Supplier<T> supplier) {
        super(template);
        contentPool = new ObjectPool<>(supplier, 120000);
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
        contentPool.delete();
    }
}
