package de.undefinedhuman.sandboxgame.entity.ecs.components.stats.food;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

public class FoodComponent extends Component {

    private float maxFood, currentFood;

    public FoodComponent(Entity entity, float maxFood) {

        super(entity);
        this.maxFood = maxFood;
        this.currentFood = maxFood;
        this.type = ComponentType.FOOD;

    }

    public float getMaxFood() {
        return maxFood;
    }

    public void setMaxFood(float maxFood) {
        this.maxFood = maxFood;
    }

    public float getCurrentFood() {
        return currentFood;
    }

    public void setCurrentFood(float currentFood) {
        this.currentFood = currentFood;
    }

    @Override
    public void setNetworkData(LineSplitter s) {

        this.currentFood = s.getNextFloat();

    }

    @Override
    public void getNetworkData(LineWriter w) {

    }

}
