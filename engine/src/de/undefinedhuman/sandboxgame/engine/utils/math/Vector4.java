package de.undefinedhuman.sandboxgame.engine.utils.math;

public class Vector4 {

    public float x, y, z, w;

    public Vector4() {
        set(0, 0, 0, 0);
    }

    public Vector4(float x, float y, float z, float w) {
        set(x, y, z, w);
    }

    public Vector4 set(Vector4 vector) {
        return set(vector.x, vector.y, vector.z, vector.w);
    }

    public Vector4 set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Vector4 add(float x, float y, float z, float w) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
        return this;
    }

    public Vector4 div(float scalar) {
        this.x /= scalar;
        this.y /= scalar;
        this.z /= scalar;
        this.w /= scalar;
        return this;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + ", " + w + "]";
    }

}
