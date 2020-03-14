package de.undefinedhuman.sandboxgameserver.entity.ecs;

import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.file.FileReader;
import de.undefinedhuman.sandboxgameserver.file.FileWriter;
import de.undefinedhuman.sandboxgameserver.file.LineSplitter;
import de.undefinedhuman.sandboxgameserver.file.LineWriter;

public abstract class Component {

    protected Entity entity;
    protected ComponentType type;

    public Component(Entity entity) {
        this.entity = entity;
    }

    public ComponentType getType() {
        return type;
    }

    public void init() {
    }

    public abstract void load(FileReader reader);

    public abstract void save(FileWriter writer);

    public abstract void getNetworkData(LineWriter w);

    public abstract void setNetworkData(LineSplitter s);

}
