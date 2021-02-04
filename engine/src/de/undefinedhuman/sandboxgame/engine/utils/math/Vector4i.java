package de.undefinedhuman.sandboxgame.engine.utils.math;

public class Vector4i {

    public int x, y, z, w;

    public Vector4i() {
        set(0, 0, 0, 0);
    }

    public Vector4i(int x, int y, int z, int w) {
        set(x, y, z, w);
    }

    public Vector4i set(Vector4i vector) {
        return set(vector.x, vector.y, vector.z, vector.w);
    }

    public Vector4i set(float x, float y, float z, float w) {
        return set((int) x, (int) y, (int) z, (int) w);
    }

    public Vector4i set(int x, int y, int z, int w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Vector4i add(int x, int y, int z, int w) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
        return this;
    }

    public Vector4i div(float scalar) {
        return div((int) scalar);
    }

    public Vector4i div(int scalar) {
        this.x /= scalar;
        this.y /= scalar;
        this.z /= scalar;
        this.w /= scalar;
        return this;
    }

    public Vector4i setY(int y) {
        this.y = y;
        return this;
    }

    public Vector4i setW(int w) {
        this.w = w;
        return this;
    }

    public boolean isEqual(Vector4i other) {
        if(this == other) return true;
        if(other == null) return false;
        return x == other.x && y == other.y && z == other.z && w == other.w;
    }

    public boolean isEqual(int x, int y, int z, int w) {
        return this.x == x && this.y == y && this.z == z && this.w == w;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + ", " + w + "]";
    }

}
