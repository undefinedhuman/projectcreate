package de.undefinedhuman.sandboxgameserver.entity.ecs.components.transform;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.file.FileReader;
import de.undefinedhuman.sandboxgameserver.file.FileWriter;
import de.undefinedhuman.sandboxgameserver.file.LineSplitter;
import de.undefinedhuman.sandboxgameserver.file.LineWriter;
import de.undefinedhuman.sandboxgameserver.world.WorldManager;

public class TransformComponent extends Component {

    private Vector2 position, size;

    public TransformComponent(Entity entity, Vector2 size) {

        super(entity);

        this.position = new Vector2(0, 0);
        this.size = size;

        this.type = ComponentType.TRANSFORM;

    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        checkWorld();
    }

    private void checkWorld() {

        if (position.x < 0.0F) position.x = WorldManager.instance.getWorld("Main").width * 16 + position.x;
        if (position.x >= WorldManager.instance.getWorld("Main").width * 16)
            position.x = 0.0F + (position.x - WorldManager.instance.getWorld("Main").width * 16);

    }

    public float getWidth() {
        return size.x;
    }

    public void setWidth(float width) {
        size.x = width;
    }

    public float getHeight() {
        return size.y;
    }

    public void setHeight(float height) {
        size.y = height;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public float getX() {
        return position.x;
    }

    public void setX(float x) {
        position.x = x;
    }

    public float getY() {
        return position.y;
    }

    public void setY(float y) {
        position.y = y;
    }

    public Vector2 getCenter() {
        return new Vector2(size.x / 2, size.y / 2);
    }

    @Override
    public void load(FileReader reader) {
        this.position = reader.getNextVector2();
    }

    @Override
    public void save(FileWriter writer) {
        writer.writeVector2(position);
    }

    @Override
    public void getNetworkData(LineWriter w) {
        w.writeVector2(position);
    }

    @Override
    public void setNetworkData(LineSplitter s) {

    }

}
