package de.undefinedhuman.projectcreate.engine.utils.math;

import com.badlogic.gdx.math.Vector2;

import java.util.Objects;

public class Vector2i {

    public int x, y;

    public Vector2i() {
        this(0, 0);
    }

    public Vector2i(int x, int y) {
        set(x, y);
    }

    public Vector2i(float x, float y) {
        set(x, y);
    }

    public Vector2i(Vector2 vector) {
        set(vector);
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

    public Vector2i add(int scalar) {
        return add(scalar, scalar);
    }

    public Vector2i add(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2i sub(int x, int y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2i sub(Vector2i vector) {
        return sub(vector.x, vector.y);
    }

    public Vector2i div(int scalar) {
        return div(scalar, scalar);
    }

    public Vector2i div(int x, int y) {
        this.x /= x;
        this.y /= y;
        return this;
    }


    public Vector2i mul(int scalar) {
        return mul(scalar, scalar);
    }

    public Vector2i mul(int x, int y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2i other = (Vector2i) o;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

}
