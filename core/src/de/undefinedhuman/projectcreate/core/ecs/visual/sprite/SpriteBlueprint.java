package de.undefinedhuman.projectcreate.core.ecs.visual.sprite;

import com.badlogic.ashley.core.Component;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentPriority;

public class SpriteBlueprint extends ComponentBlueprint {

    private SpritePanel spriteLayers = new SpritePanel("Sprite Layer");

    public SpriteBlueprint() {
        addSettings(spriteLayers);
        priority = ComponentPriority.HIGHEST;
    }

    @Override
    public Component createInstance() {
        return new SpriteComponent(spriteLayers.getValue());
    }

}
