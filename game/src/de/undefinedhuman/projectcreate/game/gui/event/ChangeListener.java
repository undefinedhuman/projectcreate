package de.undefinedhuman.projectcreate.game.gui.event;

@FunctionalInterface
public interface ChangeListener extends Listener {
    void notify(float progress);
}
