package de.undefinedhuman.projectcreate.core.item.listener;

import de.undefinedhuman.projectcreate.core.gui.event.Listener;

@FunctionalInterface
public interface ItemChangeListener extends Listener {
    void notify(int amount);
}
