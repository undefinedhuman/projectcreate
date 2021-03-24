package de.undefinedhuman.projectcreate.engine.entity.components.interaction;

import de.undefinedhuman.projectcreate.engine.entity.Component;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;

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
