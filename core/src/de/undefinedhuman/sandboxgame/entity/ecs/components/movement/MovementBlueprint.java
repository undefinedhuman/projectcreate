package de.undefinedhuman.sandboxgame.entity.ecs.components.movement;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentParam;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.HashMap;

public class MovementBlueprint extends ComponentBlueprint {

    private float speed, jumpSpeed, gravity;

    public MovementBlueprint() {

        this.speed = 0;
        this.jumpSpeed = 0;
        this.gravity = 0;

        this.type = ComponentType.MOVEMENT;

    }

    @Override
    public Component createInstance(Entity entity, HashMap<ComponentType, ComponentParam> params) {

        return new MovementComponent(entity, speed, jumpSpeed, gravity);

    }

    @Override
    public void load(HashMap<String, LineSplitter> settings, int id) {

        this.speed = Tools.loadFloat(settings,"Speed",0);
        this.jumpSpeed = Tools.loadFloat(settings,"Jump-Speed",0);
        this.gravity = Tools.loadFloat(settings,"Gravity",0);

    }

    @Override
    public void delete() {}

}
