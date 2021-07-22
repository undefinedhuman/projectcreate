package de.undefinedhuman.projectcreate.core.ecs.health;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.settings.types.Vector2Setting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

public class HealthBlueprint extends ComponentBlueprint {

    public IntSetting
            maxHealth = new IntSetting("Max Health", 0);

    public Vector2Setting
            profileImageOffset = new Vector2Setting("Profile Offset", new Vector2());

    public HealthBlueprint() {
        settings.addSettings(maxHealth, profileImageOffset);
    }

    @Override
    public Component createInstance() {
        return new HealthComponent(maxHealth.getValue());
    }

    @Override
    public void delete() {}

}
