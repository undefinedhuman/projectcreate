package de.undefinedhuman.sandboxgame.engine.entity.components.collision;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.utils.math.Vector4;

public class CollisionComponent extends Component {

    private Vector2 position = new Vector2(), size = new Vector2(), offset = new Vector2();
    private Vector2[] bounds = new Vector2[] {
            new Vector2(),
            new Vector2(),
            new Vector2(),
            new Vector2(),
            new Vector2(),
            new Vector2(),
            new Vector2()
    };

    public CollisionComponent(Vector2 size, Vector2 offset) {
        this.size.set(size);
        this.offset.set(offset);
        this.type = ComponentType.COLLISION;
    }

    public CollisionComponent updateHitbox(Vector2 position) {
        this.position.set(position).add(offset);

        for(int i = 0; i <= 1; i++)
            for(int j = 0; j <= 1; j++)
                bounds[i * 2 + j].set(this.position.x + i * size.x, this.position.y + j * size.y);

        bounds[4].set(this.position.x + size.x * 0.2f, this.position.y);
        bounds[5].set(this.position.x + size.x * 0.5f, this.position.y);
        bounds[6].set(this.position.x + size.x * 0.8f, this.position.y);
        return this;
    }

    public Vector2 bottomLeft() { return bounds[0]; }
    public Vector2 upperLeft() { return bounds[1]; }
    public Vector2 bottomRight() { return bounds[2]; }
    public Vector2 upperRight() { return bounds[3]; }

    public Vector2 bound(int index) { return bounds[index]; }

    public Vector2 getOffset() {
        return offset;
    }
    public Vector2 getSize() {
        return size;
    }

    public Vector4 getBounds(Vector2 position) {
        return new Vector4(position.x + offset.x, position.y + offset.y, position.x + offset.x + size.x, position.y + offset.y + size.y);
    }

}
