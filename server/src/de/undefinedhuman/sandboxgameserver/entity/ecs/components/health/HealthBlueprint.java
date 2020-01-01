package de.undefinedhuman.sandboxgameserver.entity.ecs.components.health;

import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.file.FileReader;

public class HealthBlueprint extends ComponentBlueprint {

    private float maxHealth;

    public HealthBlueprint() {

        this.maxHealth = 0;
        this.type = ComponentType.HEALTH;

    }

    @Override
    public Component createInstance(Entity entity) {

        return new HealthComponent(entity, maxHealth);

    }

    @Override
    public void load(FileReader reader, int id) {

        this.maxHealth = reader.getNextFloat();

    }

    @Override
    public void delete() {
    }

}
