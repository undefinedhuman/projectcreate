package de.undefinedhuman.projectcreate.engine.entity.components.collision;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector4;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector4i;
import de.undefinedhuman.projectcreate.engine.collision.Hitbox;
import de.undefinedhuman.projectcreate.engine.entity.Component;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

public class CollisionComponent extends Component {

    public boolean onSlope = false;

    private Vector2 position = new Vector2(), size = new Vector2(), offset = new Vector2();
    private Vector4i collisionBounds = new Vector4i();
    private Hitbox hitbox;

    public CollisionComponent(Vector2 size, Vector2 offset) {
        this.size.set(size);
        this.offset.set(offset);
        this.hitbox = new Hitbox(new Vector2(size).scl(0.5f), new Vector2[] {
                new Vector2(Variables.COLLISION_HITBOX_OFFSET, 0),
                new Vector2(size.x - Variables.COLLISION_HITBOX_OFFSET, 0),
                new Vector2(size.x, Variables.COLLISION_HITBOX_OFFSET),
                new Vector2(size.x, size.y - Variables.COLLISION_HITBOX_OFFSET),
                new Vector2(size.x - Variables.COLLISION_HITBOX_OFFSET, size.y),
                new Vector2(Variables.COLLISION_HITBOX_OFFSET, size.y),
                new Vector2(0, size.y - Variables.COLLISION_HITBOX_OFFSET),
                new Vector2(0, Variables.COLLISION_HITBOX_OFFSET),
        });
        this.type = ComponentType.COLLISION;
    }

    public CollisionComponent update(Vector2 position) {
        this.position.set(position).add(offset);
        this.hitbox.update(this.position);
        this.collisionBounds.set((int) this.position.x/Variables.BLOCK_SIZE*2-2, (int) this.position.y/Variables.BLOCK_SIZE*2-2, (int) (this.position.x+size.x)/Variables.BLOCK_SIZE*2+2, (int) (this.position.y+size.y)/Variables.BLOCK_SIZE*2+2);
        return this;
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    public Vector4i getCollisionBounds() {
        return collisionBounds;
    }

    public Vector4 getBounds(Vector2 position) {
        return new Vector4(position.x + offset.x, position.y + offset.y, position.x + offset.x + size.x, position.y + offset.y + size.y);
    }

}
