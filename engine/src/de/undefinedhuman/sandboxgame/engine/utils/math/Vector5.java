package de.undefinedhuman.sandboxgame.engine.utils.math;

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

}
