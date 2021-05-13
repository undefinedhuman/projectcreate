package de.undefinedhuman.projectcreate.core.ecs.stats.health;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentParam;
import de.undefinedhuman.projectcreate.engine.settings.types.Vector2Setting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

import java.util.HashMap;

public class HealthBlueprint extends ComponentBlueprint {

    public IntSetting
            maxHealth = new IntSetting("Max Health", 0);

    public Vector2Setting
            profileImageOffset = new Vector2Setting("Profile Offset", new Vector2());

    public HealthBlueprint() {
        super(HealthComponent.class);
        settings.addSettings(maxHealth, profileImageOffset);
    }

    @Override
    public Component createInstance(HashMap<Class<? extends Component>, ComponentParam> params) {
        return new HealthComponent(maxHealth.getValue());
    }

    @Override
    public void delete() {}

}
