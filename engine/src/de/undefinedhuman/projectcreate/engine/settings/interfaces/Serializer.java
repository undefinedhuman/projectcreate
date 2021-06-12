package de.undefinedhuman.projectcreate.engine.settings.interfaces;

@FunctionalInterface
public interface Serializer<T> {
    String serialize(T value);
}
