package de.undefinedhuman.sandboxgame.engine.utils.math;

import java.util.Objects;

public class Vector5 {

    public float x = 0, y = 0, z = 0, w = 0, v = 0;

    public Vector5() {}

    public Vector5(float x, float y, float z, float w, float v) {
        set(x, y, z, w, v);
    }

    public void set(float x, float y, float z, float w, float v) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        this.v = v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector5 vector5 = (Vector5) o;
        return Float.compare(vector5.x, x) == 0 && Float.compare(vector5.y, y) == 0 && Float.compare(vector5.z, z) == 0 && Float.compare(vector5.w, w) == 0 && Float.compare(vector5.v, v) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w, v);
    }
}
