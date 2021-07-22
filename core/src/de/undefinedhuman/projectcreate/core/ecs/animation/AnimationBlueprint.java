package de.undefinedhuman.projectcreate.core.ecs.animation;

import com.badlogic.ashley.core.Component;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.settings.panels.Panel;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.StringSetting;

public class AnimationBlueprint extends ComponentBlueprint {

    private StringSetting defaultAnimation = new StringSetting("Default Animation", "Idle");
    private Panel<Animation> animations = new AnimationPanel("Animation", new Animation());

    public AnimationBlueprint() {
        settings.addSettings(defaultAnimation, animations);
    }

    @Override
    public Component createInstance() {
        return new AnimationComponent(defaultAnimation.getValue(), animations.getPanelObjects());
    }

}
