package de.undefinedhuman.sandboxgameserver.entity.ecs.components.food;

import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.file.FileReader;
import de.undefinedhuman.sandboxgameserver.file.FileWriter;
import de.undefinedhuman.sandboxgameserver.file.LineSplitter;
import de.undefinedhuman.sandboxgameserver.file.LineWriter;

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
    public void load(FileReader reader) {
        this.currentFood = reader.getNextFloat();
    }

    @Override
    public void save(FileWriter writer) {
        writer.writeFloat(currentFood);
    }

    @Override
    public void getNetworkData(LineWriter w) {
        w.writeFloat(currentFood);
    }

    @Override
    public void setNetworkData(LineSplitter s) {
        this.currentFood = s.getNextFloat();
    }

}
