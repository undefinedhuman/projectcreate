package de.undefinedhuman.sandboxgame.entity.ecs.components.stats.health;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

public class HealthComponent extends Component {

    private float maxHealth, currentHealth;
    private Vector2 profileOffset;

    public HealthComponent(Entity entity, float maxHealth, Vector2 profileOffset) {

        super(entity);

        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.profileOffset = profileOffset;

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

    public Vector2 getProfileOffset() {
        return profileOffset;
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
    public void setNetworkData(LineSplitter s) {

        this.currentHealth = s.getNextFloat();

    }

    @Override
    public void getNetworkData(LineWriter w) {

    }

}
