package de.undefinedhuman.projectcreate.core.ecs.sprite;

import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentParam;
import de.undefinedhuman.projectcreate.engine.settings.panels.Panel;

import java.util.HashMap;

public class SpriteBlueprint extends ComponentBlueprint {

    private Panel<SpriteLayer> spriteLayers = new SpritePanel("Sprite Layer", new SpriteLayer());

    public SpriteBlueprint() {
        super(SpriteComponent.class);
        settings.addSettings(spriteLayers);
    }

    @Override
    public Component createInstance(HashMap<Class<? extends Component>, ComponentParam> params) {
        return new SpriteComponent(spriteLayers.getPanelObjects());
    }

}
