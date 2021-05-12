package de.undefinedhuman.projectcreate.game.utils;

import com.badlogic.gdx.math.Vector2;

public class Axis {

    private float x, y;

    public Axis(float x, float y) {

        this.x = x;
        this.y = y;

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Vector2 projectPoint(Vector2 point) {

        float v1 = ((point.x * this.x) + (point.y * this.y)) / ((this.x * this.x) + (this.y * this.y));
        return new Vector2(v1 * this.x, v1 * this.y);

    }

    public float getMaxPointOnAxis(Vector2[] vectors) {

        Vector2 largest = vectors[0];
        for (Vector2 vector : vectors)
            if (skalarMultiplikation(vector) > skalarMultiplikation(largest)) largest = vector;
        return skalarMultiplikation(largest);

    }

    private float skalarMultiplikation(Vector2 vector) {

        return vector.x * this.x + vector.y * this.y;

    }

    public float getMinPointOnAxis(Vector2[] vectors) {

        Vector2 lowest = vectors[0];
        for (Vector2 vector : vectors) if (skalarMultiplikation(vector) < skalarMultiplikation(lowest)) lowest = vector;
        return skalarMultiplikation(lowest);

    }

}
