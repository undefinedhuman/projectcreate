package de.undefinedhuman.sandboxgame.engine.entity.components.stats.food;

import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentParam;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;

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
