package de.undefinedhuman.projectcreate.core.ecs.interaction;

import de.undefinedhuman.projectcreate.engine.ecs.Component;

public class InteractionComponent extends Component {

    public boolean pressed = false;

    private int range, inputKey;

    public InteractionComponent(int range, int inputKey) {
        this.range = range;
        this.inputKey = inputKey;
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
