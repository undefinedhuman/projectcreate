package de.undefinedhuman.sandboxgame.gui.event;

@FunctionalInterface
public interface ChangeListener extends Listener {
    void notify(float progress);
}
