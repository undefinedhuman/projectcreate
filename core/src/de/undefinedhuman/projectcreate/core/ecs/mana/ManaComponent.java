package de.undefinedhuman.projectcreate.core.ecs.mana;

import com.badlogic.ashley.core.Component;

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
