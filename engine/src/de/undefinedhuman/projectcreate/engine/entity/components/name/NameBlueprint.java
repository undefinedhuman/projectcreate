package de.undefinedhuman.projectcreate.engine.entity.components.name;

import de.undefinedhuman.projectcreate.engine.entity.Component;
import de.undefinedhuman.projectcreate.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;

import java.util.HashMap;

public class NameBlueprint extends ComponentBlueprint {

    public Setting name = new Setting(SettingType.String, "Name", "");

    public NameBlueprint() {
        settings.addSettings(name);
        this.type = ComponentType.NAME;
    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        if (params.containsKey(ComponentType.NAME)) this.name.setValue(((NameParam)params.get(ComponentType.NAME)).getName());
        return new NameComponent(name.getString());
    }

    @Override
    public void delete() {}

}
