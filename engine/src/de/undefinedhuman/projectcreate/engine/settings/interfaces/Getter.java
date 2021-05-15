package de.undefinedhuman.projectcreate.engine.settings.interfaces;

@FunctionalInterface
public interface Getter<T> {
    T get(Object value);
}
