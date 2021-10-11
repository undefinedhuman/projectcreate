package de.undefinedhuman.projectcreate.game.inventory.listener;

@FunctionalInterface
public interface ItemChangeListener {
    void notify(int amount);
}
