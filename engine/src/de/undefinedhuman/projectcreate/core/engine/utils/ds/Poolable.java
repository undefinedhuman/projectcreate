package de.undefinedhuman.projectcreate.core.engine.utils.ds;

public interface Poolable {
    void init();
    boolean validate();
    void delete();
}
