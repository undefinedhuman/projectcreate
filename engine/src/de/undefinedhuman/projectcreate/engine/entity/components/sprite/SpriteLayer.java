package de.undefinedhuman.projectcreate.engine.entity.components.sprite;

import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;
import de.undefinedhuman.projectcreate.engine.settings.panels.PanelObject;
import de.undefinedhuman.projectcreate.engine.settings.types.TextureSetting;

public class SpriteLayer extends PanelObject {

    public TextureSetting
            texture = new TextureSetting("Texture", "Unknown.png");

    public Setting
            renderLevel = new Setting(SettingType.Int, "Render Level", 0),
            frameCount = new Setting(SettingType.Int, "Frame Count", 0);

    public SpriteLayer() {
        settings.addSettings(texture, renderLevel, frameCount);
    }

}
