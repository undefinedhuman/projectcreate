package de.undefinedhuman.projectcreate.engine.entity.components.movement;

import de.undefinedhuman.projectcreate.engine.entity.Component;
import de.undefinedhuman.projectcreate.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.engine.settings.Setting;

import java.util.HashMap;

public class MovementBlueprint extends ComponentBlueprint {

    private Setting
            speed = new Setting("Speed", 0),
            jumpSpeed = new Setting("Jump Speed", 0),
            gravity = new Setting("Gravity", 0),
            jumpAnimationTransition = new Setting("Jump Transition", 25);

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
