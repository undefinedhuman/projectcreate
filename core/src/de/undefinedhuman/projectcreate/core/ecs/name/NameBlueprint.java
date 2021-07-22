package de.undefinedhuman.projectcreate.core.ecs.name;

import com.badlogic.ashley.core.Component;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.StringSetting;

public class NameBlueprint extends ComponentBlueprint {

    public StringSetting name = new StringSetting("Name", "");

    public NameBlueprint() {
        settings.addSettings(name);
    }

    @Override
    public Component createInstance() {
        return new NameComponent(name.getValue());
    }

}
