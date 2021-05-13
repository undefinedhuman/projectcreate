package de.undefinedhuman.projectcreate.engine.ecs;

import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;
import de.undefinedhuman.projectcreate.engine.network.NetworkSerialization;

public abstract class Component implements NetworkSerialization {

    public Component() {}

    public void init() {}
    public void delete() {}

    public void receive(LineSplitter splitter) {}
    public void send(LineWriter writer) {}

}
