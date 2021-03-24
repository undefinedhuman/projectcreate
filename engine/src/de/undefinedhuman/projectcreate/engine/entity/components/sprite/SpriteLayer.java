package de.undefinedhuman.projectcreate.engine.entity.components.sprite;

import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;
import de.undefinedhuman.projectcreate.engine.settings.panels.PanelObject;
import de.undefinedhuman.projectcreate.engine.settings.types.BooleanSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.TextureSetting;

public class SpriteLayer extends PanelObject {

    public Setting
            texture = new TextureSetting("Texture", "Unknown.png"),
            renderLevel = new Setting(SettingType.Int, "Render Level", 0),
            isAnimated = new BooleanSetting("Is Animated", true);

    public SpriteLayer() {
        settings.addSettings(texture, renderLevel, isAnimated);
    }

}
