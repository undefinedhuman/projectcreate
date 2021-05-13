package de.undefinedhuman.projectcreate.core.ecs.movement;

import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentParam;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.FloatSetting;

import java.util.HashMap;

public class MovementBlueprint extends ComponentBlueprint {

    private FloatSetting
            speed = new FloatSetting("Speed", 0),
            jumpSpeed = new FloatSetting("Jump Speed", 0),
            gravity = new FloatSetting("Gravity", 0),
            jumpAnimationTransition = new FloatSetting("Jump Transition", 25);

    public MovementBlueprint() {
        super(MovementComponent.class);
        settings.addSettings(speed, jumpSpeed, gravity);
    }

    @Override
    public Component createInstance(HashMap<Class<? extends Component>, ComponentParam> params) {
        return new MovementComponent(speed.getValue(), jumpSpeed.getValue(), gravity.getValue(), jumpAnimationTransition.getValue());
    }

    @Override
    public void delete() {}

}
