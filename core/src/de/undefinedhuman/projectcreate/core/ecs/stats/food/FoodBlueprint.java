package de.undefinedhuman.projectcreate.core.ecs.stats.food;

import com.badlogic.ashley.core.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentPriority;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

public class FoodBlueprint extends ComponentBlueprint {

    public IntSetting maxFood = new IntSetting("Max Food", 0);

    public FoodBlueprint() {
        addSettings(maxFood);
        priority = ComponentPriority.MEDIUM;
    }

    @Override
    public Component createInstance() {
        return new FoodComponent(maxFood.getValue());
    }

}
