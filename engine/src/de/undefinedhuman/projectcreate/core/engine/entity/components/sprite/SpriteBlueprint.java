package de.undefinedhuman.projectcreate.core.engine.entity.components.sprite;

import de.undefinedhuman.projectcreate.core.engine.entity.Component;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.core.engine.settings.Setting;
import de.undefinedhuman.projectcreate.core.engine.settings.SettingType;
import de.undefinedhuman.projectcreate.core.engine.settings.panels.Panel;
import de.undefinedhuman.projectcreate.core.engine.settings.panels.StringPanel;

import java.util.HashMap;

public class SpriteBlueprint extends ComponentBlueprint {

    private Setting frameCount = new Setting(SettingType.Int, "Frame Count", 1);
    private Panel<SpriteLayer> spriteLayers = new StringPanel<>("Sprite Layer", new SpriteLayer());

    public SpriteBlueprint() {
        settings.addSettings(frameCount, spriteLayers);
        this.type = ComponentType.SPRITE;
    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        return new SpriteComponent(frameCount.getInt(), spriteLayers.getPanelObjects());
    }

}
