package de.undefinedhuman.projectcreate.core.ecs.stats.food;

import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentParam;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

import java.util.HashMap;

public class FoodBlueprint extends ComponentBlueprint {

    public IntSetting maxFood = new IntSetting("Max Food", 0);

    public FoodBlueprint() {
        super(FoodComponent.class);
        settings.addSettings(maxFood);
    }

    @Override
    public Component createInstance(HashMap<Class<? extends Component>, ComponentParam> params) {
        return new FoodComponent(maxFood.getValue());
    }

    @Override
    public void delete() {}

}
