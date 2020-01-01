package de.undefinedhuman.sandboxgameserver.entity.ecs.components.name;

import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.file.FileReader;

public class NameBlueprint extends ComponentBlueprint {

    private String name;

    public NameBlueprint() {

        this.name = "";
        this.type = ComponentType.NAME;

    }

    @Override
    public Component createInstance(Entity entity) {

        return new NameComponent(entity, name);

    }

    @Override
    public void load(FileReader reader, int id) {

        this.name = reader.getNextString();

    }

    @Override
    public void delete() {
    }

}
