package de.undefinedhuman.projectcreate.engine.utils.ds;

import java.util.Objects;

public class Tuple<T, U> {
    private T t;
    private U u;

    public Tuple(T t, U u) {
        this.t = t;
        this.u = u;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public U getU() {
        return u;
    }

    public void setU(U u) {
        this.u = u;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> other = (Tuple<?, ?>) o;
        return Objects.equals(t, other.t) && Objects.equals(u, other.u);
    }

    @Override
    public int hashCode() {
        int result = t != null ? t.hashCode() : 0;
        result = 31 * result + (u != null ? u.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "[" + t + ", " + u + "]";
    }
}
