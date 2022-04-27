package de.undefinedhuman.projectcreate.core.ecs.visual.sprite;

import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentPriority;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.RequiredComponents;

@RequiredComponents(TransformBlueprint.class)
public class SpriteBlueprint extends ComponentBlueprint {

    public final SpritePanel spriteLayers = new SpritePanel("Sprite Layer");

    public SpriteBlueprint() {
        addSettings(spriteLayers);
        priority = ComponentPriority.HIGHEST;
    }

    @Override
    public Component createInstance() {
        return new SpriteComponent(spriteLayers.getValue());
    }

}
