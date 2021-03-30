package de.undefinedhuman.projectcreate.core.gui.event;

@FunctionalInterface
public interface ChangeListener extends Listener {
    void notify(float progress);
}
