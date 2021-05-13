package de.undefinedhuman.projectcreate.engine.settings;

@FunctionalInterface
public interface Getter<T> {
    T get(Object value);
}
