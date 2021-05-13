package de.undefinedhuman.projectcreate.core.ecs.name;

import de.undefinedhuman.projectcreate.engine.ecs.ComponentParam;

public class NameParam extends ComponentParam {

    private String name;

    public NameParam(String name) {
        super(NameComponent.class);
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
