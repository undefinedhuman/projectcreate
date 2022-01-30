package de.undefinedhuman.projectcreate.core.ecs.base.type;

import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.settings.types.selection.EnumSetting;

public class TypeBlueprint extends ComponentBlueprint {

    private EnumSetting<EntityType> type = new EnumSetting<>("Type", EntityType.values(), EntityType::valueOf);

    public TypeBlueprint() {
        addSettings(type);
    }

    @Override
    public Component createInstance() {
        return new TypeComponent(type.getValue());
    }
}
