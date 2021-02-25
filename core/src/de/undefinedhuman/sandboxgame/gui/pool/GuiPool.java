package de.undefinedhuman.sandboxgame.gui.pool;

import de.undefinedhuman.sandboxgame.engine.utils.ds.ObjectPool;
import de.undefinedhuman.sandboxgame.gui.Gui;

import java.util.function.Supplier;

public class GuiPool<T extends Gui> extends ObjectPool<T> {

    private Supplier<T> guiSupplier;

    public GuiPool(Supplier<T> guiSupplier, long timeUntilDeletion) {
        super(timeUntilDeletion);
        this.guiSupplier = guiSupplier;
    }

    @Override
    protected T createInstance() {
        T gui = guiSupplier.get();
        gui.init();
        return gui;
    }

    @Override
    public boolean validate(Gui gui) {
        return true;
    }

    @Override
    public void delete(Gui gui) {
        gui.delete();
    }

}
