package de.undefinedhuman.projectcreate.core.ecs.movement;

import com.badlogic.ashley.core.Component;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.FloatSetting;

public class MovementBlueprint extends ComponentBlueprint {

    private FloatSetting
            speed = new FloatSetting("Speed", 0f),
            jumpSpeed = new FloatSetting("Jump Speed", 0f),
            gravity = new FloatSetting("Gravity", 0f),
            jumpAnimationTransition = new FloatSetting("Jump Transition", 25f);

    public MovementBlueprint() {
        settings.addSettings(speed, jumpSpeed, gravity, jumpAnimationTransition);
    }

    @Override
    public Component createInstance() {
        return new MovementComponent(speed.getValue(), jumpSpeed.getValue(), gravity.getValue(), jumpAnimationTransition.getValue());
    }
}
