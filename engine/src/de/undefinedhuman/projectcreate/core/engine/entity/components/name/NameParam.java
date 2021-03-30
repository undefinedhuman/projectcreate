package de.undefinedhuman.projectcreate.core.engine.entity.components.name;

import de.undefinedhuman.projectcreate.core.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentType;

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
