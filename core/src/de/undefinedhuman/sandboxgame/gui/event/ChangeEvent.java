package de.undefinedhuman.sandboxgame.gui.event;

@FunctionalInterface
public interface ChangeEvent {
    void notify(float progress);
}
