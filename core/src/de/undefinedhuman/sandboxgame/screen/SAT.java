package de.undefinedhuman.sandboxgame.screen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class SAT {

    public static Vector3 collide(Hitbox hitbox1, Hitbox hitbox2) {
        float overlap = Float.MAX_VALUE;
        Vector2 smallest = new Vector2();
        Vector2[] axes = hitbox1.getAxes();
        for (Vector2 axis : axes) {
            if(axis.isZero()) continue;
            Vector2 p1 = createProjection(axis, hitbox1.getVertices());
            Vector2 p2 = createProjection(axis, hitbox2.getVertices());
            if (!overlap(p1, p2)) return null;
            else {
                float o = getOverlap(p1, p2);
                if ((o != 0 || overlap == Float.MAX_VALUE)  && o < overlap) {
                    overlap = o;
                    smallest.set(axis);
                }
            }
        }

        axes = hitbox2.getAxes();
        for (Vector2 axis : axes) {
            if(axis.isZero()) continue;
            Vector2 p1 = createProjection(axis, hitbox2.getVertices());
            Vector2 p2 = createProjection(axis, hitbox1.getVertices());
            if (!overlap(p1, p2)) return null;
            else {
                float o = getOverlap(p1, p2);
                if ((o != 0 || overlap == Float.MAX_VALUE)  && o < overlap) {
                    overlap = o;
                    smallest.set(axis);
                }
            }
        }

        return new Vector3(smallest.x, smallest.y, overlap);
    }

    private static void collideAxis() {

    }

    private static Vector2 createProjection(Vector2 axis, Vector2[] vertices) {
        float min = axis.dot(vertices[0]);
        float max = min;
        for (int i = 1; i < vertices.length; i++) {
            float p = axis.dot(vertices[i]);
            if (p < min) {
                min = p;
            } else if (p > max) {
                max = p;
            }
        }
        return new Vector2(min, max);
    }

    public static boolean overlap(Vector2 projection1, Vector2 projection2) {
        return !(projection1.x > projection2.y || projection2.x > projection1.y);
    }

    public static float getOverlap(Vector2 point1, Vector2 point2) {
        return Math.min(point1.y, point2.y) - Math.max(point1.x, point2.x);
    }

}
