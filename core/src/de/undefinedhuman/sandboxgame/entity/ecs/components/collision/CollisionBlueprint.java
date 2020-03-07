package de.undefinedhuman.sandboxgame.entity.ecs.components.collision;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentParam;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.HashMap;

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
    public Component createInstance(Entity entity, HashMap<ComponentType, ComponentParam> params) {

        return new CollisionComponent(entity, width, height, offset);

    }

    @Override
    public void load(HashMap<String, LineSplitter> settings, int id) {

        this.width = Tools.loadFloat(settings, "Width", 0);
        this.height = Tools.loadFloat(settings, "Height", 0);
        this.offset = Tools.loadVector2(settings, "Offset", new Vector2(0, 0));

    }

    @Override
    public void delete() {}

}
