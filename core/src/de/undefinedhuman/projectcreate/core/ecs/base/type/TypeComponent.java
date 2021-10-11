package de.undefinedhuman.projectcreate.core.ecs.base.type;

import com.badlogic.ashley.core.Component;

public class TypeComponent implements Component {

    private EntityType type;

    public TypeComponent(EntityType type) {
        this.type = type;
    }

    public EntityType getType() {
        return type;
    }
}
