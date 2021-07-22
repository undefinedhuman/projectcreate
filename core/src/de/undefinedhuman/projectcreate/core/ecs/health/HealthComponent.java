package de.undefinedhuman.projectcreate.core.ecs.health;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component {

    private float maxHealth, currentHealth;

    public HealthComponent(float maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(float currentHealth) {
        this.currentHealth = currentHealth;
    }

    public void damage(float damage) {
        this.currentHealth -= damage;
    }

}
