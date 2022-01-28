package de.undefinedhuman.projectcreate.core.ecs.base.type;

import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.settings.types.selection.SelectionSetting;

public class TypeBlueprint extends ComponentBlueprint {

    private SelectionSetting<EntityType> type = new SelectionSetting<>("Type", EntityType.values(), EntityType::valueOf, Enum::name);

    public TypeBlueprint() {
        addSettings(type);
    }

    @Override
    public Component createInstance() {
        return new TypeComponent(type.getValue());
    }
}
