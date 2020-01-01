package de.undefinedhuman.sandboxgame.entity.ecs.components.collision;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

public class CollisionComponent extends Component {

    private float width, height;
    private Vector2 offset;

    public CollisionComponent(Entity entity, float width, float height, Vector2 offset) {

        super(entity);

        this.width = width;
        this.height = height;
        this.offset = offset;

        this.type = ComponentType.COLLISION;

    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Vector2 getOffset() {
        return offset;
    }

    public void setOffset(Vector2 offset) {
        this.offset = offset;
    }

    public Vector2[] getVertices(Vector2 pos) {

        Vector2[] vec = new Vector2[4];
        float x = pos.x + offset.x, y = pos.y + offset.y;
        vec[0] = new Vector2(x, y);
        vec[1] = new Vector2(x,y + height);
        vec[3] = new Vector2(x + width,y);
        vec[2] = new Vector2(x + width,y + height);

        return vec;

    }

    @Override
    public void setNetworkData(LineSplitter s) {}

    @Override
    public void getNetworkData(LineWriter w) {

    }

}
