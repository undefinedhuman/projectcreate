package de.undefinedhuman.projectcreate.core.ecs.name;

import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;

public class NameComponent extends Component {

    private String name;

    public NameComponent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void receive(LineSplitter splitter) {
        this.name = splitter.getNextString();
    }

    @Override
    public void send(LineWriter writer) {}

}
