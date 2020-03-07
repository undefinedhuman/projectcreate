package de.undefinedhuman.sandboxgame.entity.ecs.components.interaction;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

public class InteractionComponent extends Component {

    public boolean pressed = false;
    private int range, inputKey;

    public InteractionComponent(Entity entity, int range, int inputKey) {
        super(entity);
        this.range = range;
        this.inputKey = inputKey;
        this.type = ComponentType.INTERACTION;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getInputKey() {
        return inputKey;
    }

    @Override
    public void receive(LineSplitter splitter) {}

    @Override
    public void send(LineWriter writer) {}

}
