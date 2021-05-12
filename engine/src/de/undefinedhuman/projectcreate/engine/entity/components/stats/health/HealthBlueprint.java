package de.undefinedhuman.projectcreate.engine.entity.components.stats.health;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.entity.Component;
import de.undefinedhuman.projectcreate.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.types.Vector2Setting;

import java.util.HashMap;

public class HealthBlueprint extends ComponentBlueprint {

    public Setting
            maxHealth = new Setting("Max Health", 0),
            profileImageOffset = new Vector2Setting("Profile Offset", new Vector2());

    public HealthBlueprint() {
        settings.addSettings(maxHealth, profileImageOffset);
        this.type = ComponentType.HEALTH;
    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        return new HealthComponent(maxHealth.getInt());
    }

    @Override
    public void delete() {}

}
