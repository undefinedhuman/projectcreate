package de.undefinedhuman.projectcreate.core.ecs.stats.mana;

import de.undefinedhuman.projectcreate.engine.ecs.Component;

public class ManaComponent implements Component {

    private float maxMana, currentMana;

    public ManaComponent(float maxMana) {
        this.maxMana = maxMana;
        this.currentMana = maxMana;
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
