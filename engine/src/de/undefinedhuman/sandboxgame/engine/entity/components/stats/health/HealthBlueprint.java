package de.undefinedhuman.sandboxgame.engine.entity.components.stats.health;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentParam;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.types.Vector2Setting;

import java.util.HashMap;

public class HealthBlueprint extends ComponentBlueprint {

    public Setting
            maxHealth = new Setting(SettingType.Int, "Max Health", 0),
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
