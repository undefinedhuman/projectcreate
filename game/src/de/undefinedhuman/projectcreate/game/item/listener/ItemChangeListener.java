package de.undefinedhuman.projectcreate.game.item.listener;

import de.undefinedhuman.projectcreate.game.gui.event.Listener;

@FunctionalInterface
public interface ItemChangeListener extends Listener {
    void notify(int amount);
}
