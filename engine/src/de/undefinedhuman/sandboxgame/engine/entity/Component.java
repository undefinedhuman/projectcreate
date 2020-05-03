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
    public void delete() {}

    public void receive(LineSplitter splitter) {}
    public void send(LineWriter writer) {}

}
