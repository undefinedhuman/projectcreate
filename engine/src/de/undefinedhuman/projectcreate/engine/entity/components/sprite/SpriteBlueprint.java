package de.undefinedhuman.projectcreate.engine.entity.components.sprite;

import de.undefinedhuman.projectcreate.engine.entity.Component;
import de.undefinedhuman.projectcreate.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.engine.settings.panels.Panel;

import java.util.HashMap;

public class SpriteBlueprint extends ComponentBlueprint {

    private Panel<SpriteLayer> spriteLayers = new SpritePanel("Sprite Layer", new SpriteLayer());

    public SpriteBlueprint() {
        settings.addSettings(spriteLayers);
        this.type = ComponentType.SPRITE;
    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        return new SpriteComponent(spriteLayers.getPanelObjects());
    }

}
