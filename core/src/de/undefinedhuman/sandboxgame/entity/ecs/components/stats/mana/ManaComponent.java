package de.undefinedhuman.sandboxgame.entity.ecs.components.stats.mana;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

public class ManaComponent extends Component {

    private float maxMana, currentMana;

    public ManaComponent(Entity entity, float maxMana) {

        super(entity);

        this.maxMana = maxMana;
        this.currentMana = maxMana;

        this.type = ComponentType.MANA;

    }

    public void addMana(float mana) {

        currentMana += mana;
        currentMana = Math.min(currentMana, maxMana);

    }

    public void removeMana(float mana) {

        currentMana -= mana;
        currentMana = Math.max(currentMana, 0);

    }

    @Override
    public void receive(LineSplitter splitter) {

        this.currentMana = splitter.getNextFloat();

    }

    @Override
    public void send(LineWriter writer) {

    }

    public float getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(float maxMana) {
        this.maxMana = maxMana;
    }

    public float getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(float currentMana) {
        this.currentMana = currentMana;
    }

}
