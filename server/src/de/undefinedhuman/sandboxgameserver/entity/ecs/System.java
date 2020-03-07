package de.undefinedhuman.sandboxgameserver.entity.ecs;

import de.undefinedhuman.sandboxgameserver.entity.Entity;

public abstract class System {

    public System() {}

    public abstract void init(Entity entity);

    public abstract void update(float delta, Entity entity);

}
