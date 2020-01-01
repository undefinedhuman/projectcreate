package de.undefinedhuman.sandboxgame.entity.ecs.components.name;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

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
    public void setNetworkData(LineSplitter s) {
        this.name = s.getNextString();
    }

    @Override
    public void getNetworkData(LineWriter w) {

    }

}
