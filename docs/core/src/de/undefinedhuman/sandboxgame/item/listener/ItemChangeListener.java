package de.undefinedhuman.sandboxgame.item.listener;

import de.undefinedhuman.sandboxgame.gui.event.Listener;

@FunctionalInterface
public interface ItemChangeListener extends Listener {
    void notify(int amount);
}
