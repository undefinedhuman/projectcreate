package de.undefinedhuman.sandboxgame.engine.utils.math;

public class Vector2i {

    public int x, y;

    public Vector2i() {
        this(0, 0);
    }

    public Vector2i(int x, int y) {
        set(x, y);
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
