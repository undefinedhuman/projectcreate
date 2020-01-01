package de.undefinedhuman.sandboxgameserver.entity.ecs.components.name;

import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.file.FileReader;
import de.undefinedhuman.sandboxgameserver.file.FileWriter;
import de.undefinedhuman.sandboxgameserver.file.LineSplitter;
import de.undefinedhuman.sandboxgameserver.file.LineWriter;

public class NameComponent extends Component {

    private String name;

    public NameComponent(Entity entity, String name) {

        super(entity);
        this.name = name;
        this.type = ComponentType.NAME;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void load(FileReader reader) {
        this.name = reader.getNextString();
    }

    @Override
    public void save(FileWriter writer) {
        writer.writeString(name);
    }

    @Override
    public void getNetworkData(LineWriter w) {
        w.writeString(name);
    }

    @Override
    public void setNetworkData(LineSplitter s) {
        this.name = s.getNextString();
    }

}
