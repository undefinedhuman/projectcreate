package de.undefinedhuman.sandboxgame.entity.ecs;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.entity.Entity;

public abstract class Component {

    protected Entity entity;
    protected ComponentType type;

    public Component(Entity entity) {
        this.entity = entity;
    }

    public ComponentType getType() {
        return type;
    }

    public void init() {}
    public abstract void setNetworkData(LineSplitter s);
    public abstract void getNetworkData(LineWriter w);

}
