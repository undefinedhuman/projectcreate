package de.undefinedhuman.projectcreate.engine.ecs;

import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;
import de.undefinedhuman.projectcreate.engine.network.NetworkComponent;

public abstract class Component implements NetworkComponent {

    public Component() {}

    public void init() {}
    public void delete() {}

    @Override
    public void send(LineWriter writer) {}

    @Override
    public void receive(LineSplitter splitter) {}
}
