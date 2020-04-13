package de.undefinedhuman.sandboxgame.engine.entity.components.collision;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;

public class CollisionComponent extends Component {

    private Vector2 size, offset;

    public CollisionComponent(Vector2 size, Vector2 offset) {
        this.size = size;
        this.offset = offset;
        this.type = ComponentType.COLLISION;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
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
        vec[1] = new Vector2(x, y + size.y);
        vec[3] = new Vector2(x + size.x, y);
        vec[2] = new Vector2(x + size.x, y + size.y);
        return vec;
    }

    @Override
    public void receive(LineSplitter splitter) {}

    @Override
    public void send(LineWriter writer) {

    }

}
