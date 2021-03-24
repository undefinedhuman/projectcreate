package de.undefinedhuman.sandboxgame.engine.entity.components.stats.food;

import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;

public class FoodComponent extends Component {

    private float maxFood, currentFood;

    public FoodComponent(float maxFood) {
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
    public void receive(LineSplitter splitter) {
        this.currentFood = splitter.getNextFloat();
    }

    @Override
    public void send(LineWriter writer) {}

}
