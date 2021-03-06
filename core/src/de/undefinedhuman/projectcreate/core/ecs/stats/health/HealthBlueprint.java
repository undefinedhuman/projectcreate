package de.undefinedhuman.projectcreate.core.ecs.stats.health;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentPriority;
import de.undefinedhuman.projectcreate.engine.settings.types.Vector2Setting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

public class HealthBlueprint extends ComponentBlueprint {

    public IntSetting
            maxHealth = new IntSetting("Max Health", 0);

    public Vector2Setting
            profileImageOffset = new Vector2Setting("Profile Offset", new Vector2());

    public HealthBlueprint() {
        addSettings(maxHealth, profileImageOffset);
        priority = ComponentPriority.MEDIUM;
    }

    @Override
    public Component createInstance() {
        return new HealthComponent(maxHealth.getValue());
    }

    @Override
    public void delete() {}

}
