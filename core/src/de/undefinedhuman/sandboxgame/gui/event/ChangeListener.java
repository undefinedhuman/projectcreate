package de.undefinedhuman.sandboxgame.gui.event;

@FunctionalInterface
public interface ChangeListener {
    void notify(float progress);
}
