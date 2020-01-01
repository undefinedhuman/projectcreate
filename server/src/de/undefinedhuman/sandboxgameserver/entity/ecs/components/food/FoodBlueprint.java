package de.undefinedhuman.sandboxgameserver.entity.ecs.components.food;

import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.file.FileReader;

public class FoodBlueprint extends ComponentBlueprint {

    private float maxFood;

    public FoodBlueprint() {

        this.maxFood = 0;
        this.type = ComponentType.FOOD;

    }

    @Override
    public Component createInstance(Entity entity) {
        return new FoodComponent(entity, maxFood);
    }

    @Override
    public void load(FileReader reader, int id) {
        this.maxFood = reader.getNextFloat();
    }

    @Override
    public void delete() {}

}
