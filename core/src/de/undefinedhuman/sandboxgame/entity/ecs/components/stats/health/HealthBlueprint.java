package de.undefinedhuman.sandboxgame.entity.ecs.components.stats.health;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentParam;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.HashMap;

public class HealthBlueprint extends ComponentBlueprint {

    private float maxHealth;
    private Vector2 profileOffset;

    public HealthBlueprint() {

        this.maxHealth = 0;
        this.profileOffset = new Vector2();
        this.type = ComponentType.HEALTH;

    }

    @Override
    public Component createInstance(Entity entity, HashMap<ComponentType, ComponentParam> params) {

        return new HealthComponent(entity, maxHealth, profileOffset);

    }

    @Override
    public void load(HashMap<String, LineSplitter> settings, int id) {

        this.maxHealth = Tools.loadFloat(settings, "MaxHealth", 0);

    }

    @Override
    public void delete() {}

}
