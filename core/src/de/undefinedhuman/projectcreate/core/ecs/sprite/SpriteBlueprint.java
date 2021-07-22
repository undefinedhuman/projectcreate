package de.undefinedhuman.projectcreate.core.ecs.sprite;

import com.badlogic.ashley.core.Component;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.settings.panels.Panel;

public class SpriteBlueprint extends ComponentBlueprint {

    private Panel<SpriteLayer> spriteLayers = new SpritePanel("Sprite Layer", new SpriteLayer());

    public SpriteBlueprint() {
        settings.addSettings(spriteLayers);
    }

    @Override
    public Component createInstance() {
        return new SpriteComponent(spriteLayers.getPanelObjects());
    }

}
