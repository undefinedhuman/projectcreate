package de.undefinedhuman.projectcreate.engine.entity;

import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;
import de.undefinedhuman.projectcreate.engine.network.NetworkSerialization;

public abstract class Component implements NetworkSerialization {

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
