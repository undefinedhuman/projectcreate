package de.undefinedhuman.projectcreate.core.ecs.animation;

import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentParam;
import de.undefinedhuman.projectcreate.engine.settings.panels.Panel;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.StringSetting;

import java.util.HashMap;

public class AnimationBlueprint extends ComponentBlueprint {

    private StringSetting defaultAnimation = new StringSetting("Default Animation", "Idle");
    private Panel<Animation> animations = new AnimationPanel("Animation", new Animation());

    public AnimationBlueprint() {
        super(AnimationComponent.class);
        settings.addSettings(defaultAnimation, animations);
    }

    @Override
    public Component createInstance(HashMap<Class<? extends Component>, ComponentParam> params) {
        return new AnimationComponent(defaultAnimation.getValue(), animations.getPanelObjects());
    }

    @Override
    public void delete() {}

}
