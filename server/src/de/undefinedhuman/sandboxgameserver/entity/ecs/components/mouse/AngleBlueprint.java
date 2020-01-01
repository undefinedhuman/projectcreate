package de.undefinedhuman.sandboxgameserver.entity.ecs.components.mouse;

import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.file.FileReader;

public class AngleBlueprint extends ComponentBlueprint {

    public AngleBlueprint() {

        this.type = ComponentType.ANGLE;

    }

    @Override
    public Component createInstance(Entity entity) {

        return new AngleComponent(entity);

    }

    @Override
    public void load(FileReader reader, int id) {
    }

    @Override
    public void delete() {
    }

}
