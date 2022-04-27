package de.undefinedhuman.projectcreate.engine.gui.event;

@FunctionalInterface
public interface ChangeListener extends Listener {
    void notify(float progress);
}
