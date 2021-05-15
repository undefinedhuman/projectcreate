package de.undefinedhuman.projectcreate.engine.settings.interfaces;

@FunctionalInterface
public interface Parser<T> {
    T get(String value);
}
