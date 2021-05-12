package de.undefinedhuman.projectcreate.engine.entity.components.stats.food;

import de.undefinedhuman.projectcreate.engine.entity.Component;
import de.undefinedhuman.projectcreate.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.engine.settings.Setting;

import java.util.HashMap;

public class FoodBlueprint extends ComponentBlueprint {

    public Setting maxFood = new Setting("Max Food", 0);

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
