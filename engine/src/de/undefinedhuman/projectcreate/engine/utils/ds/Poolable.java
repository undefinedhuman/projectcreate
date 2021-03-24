package de.undefinedhuman.projectcreate.engine.utils.ds;

public interface Poolable {
    void init();
    boolean validate();
    void delete();
}
