package de.undefinedhuman.projectcreate.core.ecs.stats.health;

import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;

public class HealthComponent extends Component {

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

    @Override
    public void receive(LineSplitter splitter) {
        this.currentHealth = splitter.getNextFloat();
    }

    @Override
    public void send(LineWriter writer) {}

}
