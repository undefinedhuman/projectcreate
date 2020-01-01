package de.undefinedhuman.sandboxgameserver.entity.ecs.components.movement;

import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.file.FileReader;

public class MovementBlueprint extends ComponentBlueprint {

    private float speed, jumpSpeed, gravity;

    public MovementBlueprint() {

        this.speed = 0;
        this.jumpSpeed = 0;
        this.gravity = 0;

        this.type = ComponentType.MOVEMENT;

    }

    @Override
    public Component createInstance(Entity entity) {

        return new MovementComponent(entity, speed, jumpSpeed, gravity);

    }

    @Override
    public void load(FileReader reader, int id) {

        this.speed = reader.getNextFloat();
        this.jumpSpeed = reader.getNextFloat();
        this.gravity = reader.getNextFloat();

    }

    @Override
    public void delete() {
    }

}
