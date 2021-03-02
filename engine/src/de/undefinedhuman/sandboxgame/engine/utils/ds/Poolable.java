package de.undefinedhuman.sandboxgame.engine.utils.ds;

public interface Poolable {
    void init();
    boolean validate();
    void delete();
}
