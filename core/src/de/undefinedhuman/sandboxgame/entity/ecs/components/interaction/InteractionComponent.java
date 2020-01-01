package de.undefinedhuman.sandboxgame.entity.ecs.components.interaction;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

public class InteractionComponent extends Component {

    private int range, inputKey;

    public boolean pressed = false;

    public InteractionComponent(Entity entity, int range, int inputKey) {
        super(entity);
        this.range = range;
        this.inputKey = inputKey;
        this.type = ComponentType.INTERACTION;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getRange() {
        return range;
    }

    public int getInputKey() {
        return inputKey;
    }

    @Override
    public void setNetworkData(LineSplitter s) {}

    @Override
    public void getNetworkData(LineWriter w) {}

}
