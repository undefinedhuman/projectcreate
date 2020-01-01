package de.undefinedhuman.sandboxgame.entity.ecs.components.mouse;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentParam;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

import java.util.HashMap;

public class AngleBlueprint extends ComponentBlueprint {

    public AngleBlueprint() {
        this.type = ComponentType.ANGLE;
    }

    @Override
    public Component createInstance(Entity entity, HashMap<ComponentType, ComponentParam> params) {
        return new AngleComponent(entity);
    }

    @Override
    public void load(HashMap<String, LineSplitter> settings, int id) {

    }

    @Override
    public void delete() {}

}
