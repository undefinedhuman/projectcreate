package de.undefinedhuman.sandboxgame.engine.collision;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;

public class Hitbox {

    private static final int VERTICES_TEMPLATE = 0;
    private static final int VERTICES_CURRENT = 1;
    private static final int AXES = 2;

    private Vector2 position = new Vector2();

    private Vector2[][] collisionData;
    private int verticesLength;

    private Vector2 center = new Vector2();

    public Hitbox(Vector2 center, Vector2[] verticesTemplate) {
        this.center.set(center);
        this.collisionData = new Vector2[3][verticesLength = verticesTemplate.length];
        this.collisionData[VERTICES_TEMPLATE] = verticesTemplate;
        for(int i = 0; i < verticesLength; i++) collisionData[VERTICES_CURRENT][i] = new Vector2();
        calculateAxes();
    }

    public void update(Vector2 position) {
        this.update(position.x, position.y);
    }

    public void update(float x, float y) {
        this.position.set(x, y);
        for(int i = 0; i < verticesLength; i++) {
            Vector2 vertexTemplate = collisionData[VERTICES_TEMPLATE][i];
            collisionData[VERTICES_CURRENT][i].set(position).add(vertexTemplate);
        }
    }

    public void render(SpriteBatch batch) {
        for(int i = 0; i < verticesLength; i++)
            Tools.drawLine(batch, collisionData[VERTICES_CURRENT][i], collisionData[VERTICES_CURRENT][(i+1)%verticesLength], 1, Variables.HITBOX_COLOR);
    }

    public Vector2 getCenter() {
        return new Vector2(position).add(center);
    }

    public Vector2[] getAxes() {
        return collisionData[AXES];
    }

    public Vector2[] getVertices() {
        return collisionData[VERTICES_CURRENT];
    }


    private void calculateAxes() {
        for (int i = 0; i < verticesLength; i++) {
            Vector2 edge = new Vector2(collisionData[VERTICES_TEMPLATE][i]).sub(collisionData[VERTICES_TEMPLATE][(i+1)%verticesLength]);
            collisionData[AXES][i] = new Vector2(edge.y, -edge.x).nor();
        }
    }

}
