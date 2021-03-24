package de.undefinedhuman.projectcreate.engine.collision;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class SAT {

    public static Vector3 collide(Hitbox hitbox1, Hitbox hitbox2) {
        Vector3 mtv = new Vector3(0, 0, Float.MAX_VALUE);
        if(!collideAxis(hitbox1.getAxes(), hitbox1, hitbox2, mtv)) return null;
        if(!collideAxis(hitbox2.getAxes(), hitbox1, hitbox2, mtv)) return null;
        return mtv;
    }

    private static boolean collideAxis(Vector2[] axes, Hitbox hitbox1, Hitbox hitbox2, Vector3 mtv) {
        for (Vector2 axis : axes) {
            if(axis.isZero()) continue;
            Vector2 p1 = createProjection(axis, hitbox1.getVertices()), p2 = createProjection(axis, hitbox2.getVertices());
            if (!overlap(p1, p2)) return false;
            else {
                float overlap = getOverlap(p1, p2);
                if (overlap < mtv.z)
                    mtv.set(axis.x, axis.y, overlap);
            }
        }

        return true;
    }

    private static Vector2 createProjection(Vector2 axis, Vector2[] vertices) {
        float min = axis.dot(vertices[0]);
        float max = min;
        for (int i = 1; i < vertices.length; i++) {
            float p = axis.dot(vertices[i]);
            if (p < min) min = p;
            else if (p > max) max = p;
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
