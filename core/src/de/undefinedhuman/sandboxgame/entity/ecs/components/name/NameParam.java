package de.undefinedhuman.sandboxgame.entity.ecs.components.name;

import de.undefinedhuman.sandboxgame.entity.ecs.ComponentParam;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

public class NameParam extends ComponentParam {

    private String name;

    public NameParam(String name) {

        super(ComponentType.NAME);
        this.name = name;

    }

    public String getName() {
        return name;
    }

}
