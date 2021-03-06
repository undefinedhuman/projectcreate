package de.undefinedhuman.projectcreate.core.ecs.player.movement;

import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.visual.animation.AnimationBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentPriority;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.RequiredComponents;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.FloatSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.selection.DynamicStringSelectionSetting;

@RequiredComponents(TransformBlueprint.class)
public class MovementBlueprint extends ComponentBlueprint {

    private FloatSetting
            speed = new FloatSetting("Speed", 0f),
            jumpSpeed = new FloatSetting("Jump Speed", 0f),
            gravity = new FloatSetting("Gravity", 0f),
            jumpAnimationTransition = new FloatSetting("Jump Transition", 25f);

    private DynamicStringSelectionSetting
            idleAnimation = new DynamicStringSelectionSetting("Idle Animation", this::getAnimationNames),
            runAnimation = new DynamicStringSelectionSetting("Run Animation", this::getAnimationNames),
            jumpAnimation = new DynamicStringSelectionSetting("Jump Animation", this::getAnimationNames),
            transitionAnimation = new DynamicStringSelectionSetting("TransitionAnimation", this::getAnimationNames),
            fallAnimation = new DynamicStringSelectionSetting("Fall Animation", this::getAnimationNames);

    public MovementBlueprint() {
        addSettings(speed, jumpSpeed, gravity, jumpAnimationTransition, idleAnimation, runAnimation, jumpAnimation, transitionAnimation, fallAnimation);
        priority = ComponentPriority.HIGH;
    }

    @Override
    public Component createInstance() {
        return new MovementComponent(speed.getValue(), jumpSpeed.getValue(), gravity.getValue(), jumpAnimationTransition.getValue(), idleAnimation.getValue(), runAnimation.getValue(), jumpAnimation.getValue(), transitionAnimation.getValue(), fallAnimation.getValue());
    }

    private String[] getAnimationNames() {
        AnimationBlueprint blueprint = BlueprintManager.getInstance().getBlueprint(blueprintID).getComponentBlueprint(AnimationBlueprint.class);
        if(blueprint == null)
            return new String[0];
        return blueprint.animations.getValue().keySet().stream().sorted().toArray(String[]::new);
    }

}
