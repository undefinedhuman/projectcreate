package de.undefinedhuman.sandboxgameserver.entity.ecs.components.collision;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.entity.ecs.components.transform.TransformComponent;
import de.undefinedhuman.sandboxgameserver.file.FileReader;
import de.undefinedhuman.sandboxgameserver.file.FileWriter;
import de.undefinedhuman.sandboxgameserver.file.LineSplitter;
import de.undefinedhuman.sandboxgameserver.file.LineWriter;

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

    public Vector2[] getVertices(TransformComponent transformComponent) {

        Vector2[] vec = new Vector2[4];
        vec[0] = new Vector2(transformComponent.getX() + offset.x, transformComponent.getY() + offset.y);
        vec[1] = new Vector2(transformComponent.getX() + offset.x + width, transformComponent.getY() + offset.y);
        vec[3] = new Vector2(transformComponent.getX() + offset.x + width, transformComponent.getY() + offset.y + height);
        vec[2] = new Vector2(transformComponent.getX() + offset.x, transformComponent.getY() + offset.y + height);

        return vec;

    }

    @Override
    public void load(FileReader reader) { }

    @Override
    public void save(FileWriter writer) { }

    @Override
    public void getNetworkData(LineWriter w) { }

    @Override
    public void setNetworkData(LineSplitter s) { }

}
