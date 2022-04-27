package de.undefinedhuman.projectcreate.engine.event;

@FunctionalInterface
public interface Observer<E extends Event> {
    void notify(E e);
}
