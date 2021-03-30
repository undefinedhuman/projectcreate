package de.undefinedhuman.projectcreate.engine.entity.components.sprite;

import de.undefinedhuman.projectcreate.engine.entity.Component;
import de.undefinedhuman.projectcreate.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;
import de.undefinedhuman.projectcreate.engine.settings.panels.Panel;
import de.undefinedhuman.projectcreate.engine.settings.panels.StringPanel;

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
