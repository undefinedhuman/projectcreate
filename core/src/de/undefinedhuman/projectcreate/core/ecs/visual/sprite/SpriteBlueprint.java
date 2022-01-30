package de.undefinedhuman.projectcreate.core.ecs.visual.sprite;

import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentPriority;

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
