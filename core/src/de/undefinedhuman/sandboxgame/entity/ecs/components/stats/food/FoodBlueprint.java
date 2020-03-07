package de.undefinedhuman.sandboxgame.entity.ecs.components.stats.food;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentParam;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.HashMap;

public class FoodBlueprint extends ComponentBlueprint {

    private float maxFood;

    public FoodBlueprint() {
        this.maxFood = 0;
        this.type = ComponentType.FOOD;
    }

    @Override
    public Component createInstance(Entity entity, HashMap<ComponentType, ComponentParam> params) {
        return new FoodComponent(entity, maxFood);
    }

    @Override
    public void load(HashMap<String, LineSplitter> settings, int id) {
        this.maxFood = Tools.loadFloat(settings, "MaxFood", 0);
    }

    @Override
    public void delete() {}

}
