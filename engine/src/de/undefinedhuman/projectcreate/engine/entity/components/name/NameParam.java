package de.undefinedhuman.projectcreate.engine.entity.components.name;

import de.undefinedhuman.projectcreate.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;

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
