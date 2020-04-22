package de.undefinedhuman.sandboxgame.engine.entity.components.sprite;

import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentParam;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.panels.Panel;

import java.util.HashMap;

public class SpriteBlueprint extends ComponentBlueprint {

    private Setting frameCount = new Setting(SettingType.Int, "Frame Count", 1);
    private Panel spriteLayers = new Panel(SettingType.Sprite, "Sprite Layers", new SpriteLayer());

    public SpriteBlueprint() {
        settings.addSettings(frameCount, spriteLayers);
        this.type = ComponentType.SPRITE;
    }

    @Override
    public void load(FileReader reader) {
        super.load(reader);
        spriteLayers.loadObjects(reader);
    }

    @Override
    public void save(FileWriter writer) {
        super.save(writer);
        spriteLayers.saveObjects(writer);
    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        HashMap<String, SpriteLayer> spriteParams = new HashMap<>();
        for(String key : spriteLayers.getObjects().keySet()) spriteParams.put(key, (SpriteLayer) spriteLayers.getObjects().get(key));
        return new SpriteComponent(frameCount.getInt(), spriteParams);
    }

}
