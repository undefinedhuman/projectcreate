package de.undefinedhuman.sandboxgame.engine.entity.components.sprite;

import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentParam;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.panels.Panel;
import de.undefinedhuman.sandboxgame.engine.settings.panels.StringPanel;

import java.util.HashMap;

public class SpriteBlueprint extends ComponentBlueprint {

    private Setting frameCount = new Setting(SettingType.Int, "Frame Count", 1);
    private Panel spriteLayers = new StringPanel("Sprite Layer", new SpriteLayer());

    public SpriteBlueprint() {
        settings.addSettings(frameCount, spriteLayers);
        this.type = ComponentType.SPRITE;
    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        HashMap<String, SpriteLayer> spriteParams = new HashMap<>();
        for(String key : spriteLayers.getPanelObjects().keySet()) spriteParams.put(key, (SpriteLayer) spriteLayers.getPanelObjects().get(key));
        return new SpriteComponent(frameCount.getInt(), spriteParams);
    }

}
