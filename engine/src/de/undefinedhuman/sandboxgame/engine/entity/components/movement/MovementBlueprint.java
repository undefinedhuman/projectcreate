package de.undefinedhuman.sandboxgame.engine.entity.components.movement;

import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentParam;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;

import java.util.HashMap;

public class MovementBlueprint extends ComponentBlueprint {

    private Setting
            speed = new Setting(SettingType.Float, "Speed", 0),
            jumpSpeed = new Setting(SettingType.Float, "Jump Speed", 0),
            gravity = new Setting(SettingType.Float, "Gravity", 0),
            jumpAnimationTransition = new Setting(SettingType.Float, "Jump Transition", 25);

    public MovementBlueprint() {
        settings.addSettings(speed, jumpSpeed, gravity);
        this.type = ComponentType.MOVEMENT;
    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        return new MovementComponent(speed.getFloat(), jumpSpeed.getFloat(), gravity.getFloat(), jumpAnimationTransition.getFloat());
    }

    @Override
    public void delete() {}

}
