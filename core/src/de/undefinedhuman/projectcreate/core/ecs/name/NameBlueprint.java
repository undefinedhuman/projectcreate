package de.undefinedhuman.projectcreate.core.ecs.name;

import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentParam;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.StringSetting;

import java.util.HashMap;

public class NameBlueprint extends ComponentBlueprint {

    public StringSetting name = new StringSetting("Name", "");

    public NameBlueprint() {
        super(NameComponent.class);
        settings.addSettings(name);
    }

    @Override
    public Component createInstance(HashMap<Class<? extends Component>, ComponentParam> params) {
        if (params.containsKey(NameComponent.class)) this.name.setValue(((NameParam)params.get(NameComponent.class)).getName());
        return new NameComponent(name.getValue());
    }

}
