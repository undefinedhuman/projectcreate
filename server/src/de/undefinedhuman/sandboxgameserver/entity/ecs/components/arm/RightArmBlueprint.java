package de.undefinedhuman.sandboxgameserver.entity.ecs.components.arm;

import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.file.FileReader;

public class RightArmBlueprint extends ComponentBlueprint {

    public RightArmBlueprint() {

        type = ComponentType.RIGHTARM;

    }

    @Override
    public Component createInstance(Entity entity) {
        return new RightArmComponent(entity);
    }

    @Override
    public void load(FileReader reader, int id) {
    }

    @Override
    public void delete() {
    }

}
