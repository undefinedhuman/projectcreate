package de.undefinedhuman.projectcreate.core.engine.entity.components.stats.food;

import de.undefinedhuman.projectcreate.core.engine.entity.Component;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.core.engine.settings.Setting;
import de.undefinedhuman.projectcreate.core.engine.settings.SettingType;

import java.util.HashMap;

public class FoodBlueprint extends ComponentBlueprint {

    public Setting maxFood = new Setting(SettingType.Int, "Max Food", 0);

    public FoodBlueprint() {
        settings.addSettings(maxFood);
        this.type = ComponentType.FOOD;
    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        return new FoodComponent(maxFood.getInt());
    }

    @Override
    public void delete() {}

}
