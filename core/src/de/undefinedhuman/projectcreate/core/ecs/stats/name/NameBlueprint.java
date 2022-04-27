package de.undefinedhuman.projectcreate.core.ecs.stats.name;

import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentPriority;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.StringSetting;

public class NameBlueprint extends ComponentBlueprint {

    public StringSetting name = new StringSetting("Name", "", false);

    public NameBlueprint() {
        addSettings(name);
        priority = ComponentPriority.HIGH;
    }

    @Override
    public Component createInstance() {
        return new NameComponent(name.getValue());
    }

}
