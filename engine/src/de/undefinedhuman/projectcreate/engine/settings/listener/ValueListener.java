package de.undefinedhuman.projectcreate.engine.settings.listener;

@FunctionalInterface
public interface ValueListener<T> {
    void notify(T value);
}
