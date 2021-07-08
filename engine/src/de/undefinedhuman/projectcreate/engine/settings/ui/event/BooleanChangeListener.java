package de.undefinedhuman.projectcreate.engine.settings.ui.event;

@FunctionalInterface
public interface BooleanChangeListener {
    void notify(boolean value);
}
