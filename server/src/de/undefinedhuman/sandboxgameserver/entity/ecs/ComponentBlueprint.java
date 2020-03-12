package de.undefinedhuman.sandboxgameserver.entity.ecs;

import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgameserver.entity.Entity;

public abstract class ComponentBlueprint {

    protected ComponentType type;

    public ComponentBlueprint() {}

    public ComponentType getType() {
        return type;
    }

    public abstract Component createInstance(Entity entity);

    public abstract void load(FileReader reader, int id);

    public abstract void delete();

}
