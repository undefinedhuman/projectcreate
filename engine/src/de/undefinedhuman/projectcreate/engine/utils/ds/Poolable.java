package de.undefinedhuman.projectcreate.engine.utils.ds;

public interface Poolable {
    default void init() {}
    default boolean validate() {
        return true;
    }
    default void freeUp() {}
    default void delete() {}
}
