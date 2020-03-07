package de.undefinedhuman.sandboxgame.entity.ecs;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.network.components.NetworkComponent;

public abstract class Component implements NetworkComponent {

    protected Entity entity;
    protected ComponentType type;

    public Component(Entity entity) {
        this.entity = entity;
    }

    public ComponentType getType() {
        return type;
    }

    public void init() {}

    public abstract void send(LineWriter writer);

    public abstract void receive(LineSplitter splitter);

}
