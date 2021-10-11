package de.undefinedhuman.projectcreate.engine.utils.ds;

public interface Poolable {
    void init();
    default boolean validate() {
        return true;
    }
    void delete();
}
