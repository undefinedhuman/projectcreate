package de.undefinedhuman.sandboxgame.engine.entity.components.interaction;

import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;

public class InteractionComponent extends Component {

    public boolean pressed = false;

    private int range, inputKey;

    public InteractionComponent(int range, int inputKey) {
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

}
