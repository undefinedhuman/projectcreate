package de.undefinedhuman.projectcreate.engine.utils.ds;

@FunctionalInterface
public interface TriConsumer<T, U, E> {
    void accept(T t, U u, E e);
}
