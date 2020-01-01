package de.undefinedhuman.sandboxgame.entity.ecs.components.mouse;

import de.undefinedhuman.sandboxgame.entity.ecs.ComponentParam;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

public class AngleParam extends ComponentParam {

    private boolean isTurned;

    public AngleParam(boolean isTurned) {

        super(ComponentType.ANGLE);
        this.isTurned = isTurned;

    }

    public boolean isTurned() {
        return isTurned;
    }

}
