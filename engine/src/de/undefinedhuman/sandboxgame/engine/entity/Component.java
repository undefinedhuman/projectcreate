package de.undefinedhuman.sandboxgame.engine.entity;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.engine.network.NetworkComponent;

public abstract class Component implements NetworkComponent {

    protected ComponentType type;

    public Component() {}

    public ComponentType getType() {
        return type;
    }

    public void init() {}

    public abstract void receive(LineSplitter splitter);
    public abstract void send(LineWriter writer);

}
