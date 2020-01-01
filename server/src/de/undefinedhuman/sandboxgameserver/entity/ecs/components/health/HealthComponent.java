package de.undefinedhuman.sandboxgameserver.entity.ecs.components.health;

import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.file.FileReader;
import de.undefinedhuman.sandboxgameserver.file.FileWriter;
import de.undefinedhuman.sandboxgameserver.file.LineSplitter;
import de.undefinedhuman.sandboxgameserver.file.LineWriter;

public class HealthComponent extends Component {

    private float maxHealth, currentHealth;

    public HealthComponent(Entity entity, float maxHealth) {

        super(entity);

        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;

        this.type = ComponentType.HEALTH;

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

    public void heal(float health) {

        this.currentHealth += health;
        currentHealth = Math.min(currentHealth, maxHealth);

    }

    public void damage(float health) {

        this.currentHealth -= health;
        currentHealth = Math.max(currentHealth, 0);

    }

    @Override
    public void load(FileReader reader) {

        this.currentHealth = reader.getNextFloat();

    }

    @Override
    public void save(FileWriter writer) {

        writer.writeFloat(currentHealth);

    }

    @Override
    public void getNetworkData(LineWriter w) {
        w.writeFloat(currentHealth);
    }

    @Override
    public void setNetworkData(LineSplitter s) {
        this.currentHealth = s.getNextFloat();
    }

}
