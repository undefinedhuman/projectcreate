package de.undefinedhuman.projectcreate.core.engine.utils.math;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector4 vector4 = (Vector4) o;
        return Float.compare(vector4.x, x) == 0 && Float.compare(vector4.y, y) == 0 && Float.compare(vector4.z, z) == 0 && Float.compare(vector4.w, w) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + ", " + w + "]";
    }

}
