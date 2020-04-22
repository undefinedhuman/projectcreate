package de.undefinedhuman.sandboxgame.engine.utils.math;

import com.badlogic.gdx.math.Vector2;

public class Vector2i {

    public int x, y;

    public Vector2i() {
        this(0, 0);
    }

    public Vector2i(int x, int y) {
        set(x, y);
    }

    public Vector2i set(Vector2 vector) {
        return set((int) vector.x, (int) vector.y);
    }

    public Vector2i set(Vector2i vector) {
        return set(vector.x, vector.y);
    }

    public Vector2i set(float x, float y) {
        return set((int) x, (int) y);
    }

    public Vector2i set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2i div(int scalar) {
        this.x /= scalar;
        this.y /= scalar;
        return this;
    }

}
