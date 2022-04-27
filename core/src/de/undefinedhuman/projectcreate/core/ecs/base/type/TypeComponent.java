package de.undefinedhuman.projectcreate.core.ecs.base.type;

import de.undefinedhuman.projectcreate.engine.ecs.Component;

public class TypeComponent implements Component {

    private EntityType type;

    public TypeComponent(EntityType type) {
        this.type = type;
    }

    public EntityType getType() {
        return type;
    }
}
