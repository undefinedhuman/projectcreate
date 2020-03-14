package de.undefinedhuman.sandboxgameserver.entity.ecs.components.collision;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.file.FileReader;

public class CollisionBlueprint extends ComponentBlueprint {

    private float width, height;
    private Vector2 offset;

    public CollisionBlueprint() {

        width = 0;
        height = 0;
        offset = new Vector2(0, 0);
        this.type = ComponentType.COLLISION;

    }

    @Override
    public Component createInstance(Entity entity) {

        return new CollisionComponent(entity, width, height, offset);

    }

    @Override
    public void load(FileReader reader, int id) {

        this.width = reader.getNextFloat();
        this.height = reader.getNextFloat();
        this.offset = reader.getNextVector2();

    }

    @Override
    public void delete() {
    }

}
