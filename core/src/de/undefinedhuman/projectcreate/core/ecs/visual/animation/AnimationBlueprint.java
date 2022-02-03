package de.undefinedhuman.projectcreate.core.ecs.visual.animation;

import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentPriority;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.RequiredComponents;
import de.undefinedhuman.projectcreate.engine.settings.types.selection.DynamicSelectionSetting;

import java.util.function.Supplier;

@RequiredComponents(SpriteBlueprint.class)
public class AnimationBlueprint extends ComponentBlueprint {

    private final DynamicSelectionSetting<String> defaultAnimation = new DynamicSelectionSetting<>("Default Animation", new Supplier<String[]>() {
        @Override
        public String[] get() {
            return animations.getValue().keySet().toArray(new String[0]);
        }
    }, value -> value, value -> value);
    public final AnimationPanel animations = new AnimationPanel("Animation");

    public AnimationBlueprint() {
        addSettings(defaultAnimation, animations);
        priority = ComponentPriority.HIGHEST;
    }

    @Override
    public Component createInstance() {
        return new AnimationComponent(defaultAnimation.getValue(), animations.getValue());
    }

}
