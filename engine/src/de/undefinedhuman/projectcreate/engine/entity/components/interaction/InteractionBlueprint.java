package de.undefinedhuman.projectcreate.engine.entity.components.interaction;

import de.undefinedhuman.projectcreate.engine.entity.Component;
import de.undefinedhuman.projectcreate.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.engine.settings.Setting;

import java.util.HashMap;

public class InteractionBlueprint extends ComponentBlueprint {

    public Setting
            range = new Setting("Range", 0),
            inputKey = new Setting("Input Key", "F");

    public InteractionBlueprint() {
        settings.addSettings(range, inputKey);
        this.type = ComponentType.INTERACTION;
    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        return new InteractionComponent(range.getInt(), inputKey.getInputKey());
    }

}
