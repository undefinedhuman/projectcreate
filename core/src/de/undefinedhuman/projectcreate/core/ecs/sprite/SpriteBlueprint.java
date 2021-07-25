package de.undefinedhuman.projectcreate.core.ecs.sprite;

import com.badlogic.ashley.core.Component;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentPriority;
import de.undefinedhuman.projectcreate.engine.settings.panels.Panel;

public class SpriteBlueprint extends ComponentBlueprint {

    private Panel<SpriteLayer> spriteLayers = new SpritePanel("Sprite Layer");

    public SpriteBlueprint() {
        addSettings(spriteLayers);
        priority = ComponentPriority.HIGHEST;
    }

    @Override
    public Component createInstance() {
        return new SpriteComponent(spriteLayers.getValue());
    }

}
