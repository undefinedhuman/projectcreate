package de.undefinedhuman.projectcreate.core.ecs.stats.food;

import com.badlogic.ashley.core.Component;

public class FoodComponent implements Component {

    private float maxFood, currentFood;

    public FoodComponent(float maxFood) {
        this.maxFood = maxFood;
        this.currentFood = maxFood;
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

}
