package de.undefinedhuman.sandboxgame.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class Hitbox {

    private Vector2 position = new Vector2(), size = new Vector2();

    private int state;

    private Color color = Variables.HITBOX_COLOR;

    private Vector2[][] hitboxes = new Vector2[][] {

            new Vector2[] {
                    new Vector2(0, 0),
                    new Vector2(1, 0),
                    new Vector2(1, 1),
                    new Vector2(0, 1)
            },

            new Vector2[] {
                    new Vector2(0, -1),
                    new Vector2(1, 0),
                    new Vector2(0, 1),
                    new Vector2(-1, 0)
            }

    };

    public Hitbox(float x, float y, float width, float height, int state) {
        this.position.set(x, y);
        this.size.set(width, height);
        this.state = state;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public void addPosition(float x, float y) {
        this.position.add(x, y);
    }

    public void addPosition(Vector2 vector2) {
        this.position.add(vector2);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setPosition(Vector2 position) {
        this.position.set(position);
    }

    public Vector2 getSize() {
        return size;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getState() {
        return state;
    }

    public void render(SpriteBatch batch) {
        Vector2[] vertices = getVertices();
        int length = vertices.length;
        for(int i = 0; i < length; i++)
            Tools.drawLine(batch, vertices[(i)%vertices.length], vertices[(i+1)%vertices.length], 1, color);
    }

    public static Vector3 collideSAT(Hitbox hitbox1, Hitbox hitbox2) {
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

    private Vector2[] getAxes() {
        Vector2[] vertices = getVertices();
        Vector2[] axes = new Vector2[vertices.length];
        for (int i = 0; i < axes.length; i++) {
            Vector2 p1 = vertices[i];
            Vector2 p2 = vertices[(i+1)%vertices.length];
            Vector2 edge = new Vector2(p2).sub(p1);
            Vector2 normal = new Vector2(edge.y, -edge.x);
            axes[i] = normal.nor();
        }
        return axes;
    }

    public Vector2[] getVertices() {

        if(state == 1) {
            return new Vector2[] {
                    new Vector2(position),
                    new Vector2(position).add(size.x, 0),
                    new Vector2(position).add(size),
                    new Vector2(position).add(0, size.y)
            };
        }

        if(state == 2) {
            return new Vector2[] {
                    new Vector2(position),
                    new Vector2(position).add(0, size.y),
                    new Vector2(position).add(size)
            };
        }

        if(state == 3) {
            return new Vector2[] {
                    new Vector2(position).add(0, size.y),
                    new Vector2(position).add(size),
                    new Vector2(position).add(size.x, 0)
            };
        }

        if(state == 4) {
            return new Vector2[] {
                    new Vector2(position),
                    new Vector2(position).add(size.x, 0),
                    new Vector2(position).add(0, size.y)
            };
        }

        if(state == 5) {
            return new Vector2[] {
                    new Vector2(position),
                    new Vector2(position).add(size.x, 0),
                    new Vector2(position).add(size)
            };
        }

        if(state == -1) {
            return new Vector2[] {
                    new Vector2(position.x + 24, position.y),
                    new Vector2(position.x + size.x - 24, position.y),
                    new Vector2(position.x + size.x, position.y + 24),
                    new Vector2(position.x + size.x, position.y + size.y - 24),
                    new Vector2(position.x + size.x - 24, position.y + size.y),
                    new Vector2(position.x + 24, position.y + size.y),
                    new Vector2(position.x, position.y + size.y - 24),
                    new Vector2(position.x, position.y + 24)
            };
        }

        return null;

    }

    // Calculate Overlap: Math.min(p1.max, p2.max) - Math.max(p1.min, p2.min);

}
