package de.undefinedhuman.projectcreate.item.listener;

import de.undefinedhuman.projectcreate.gui.event.Listener;

@FunctionalInterface
public interface ItemChangeListener extends Listener {
    void notify(int amount);
}
