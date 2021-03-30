package de.undefinedhuman.projectcreate.core.engine.entity.components.sprite;

import de.undefinedhuman.projectcreate.core.engine.settings.Setting;
import de.undefinedhuman.projectcreate.core.engine.settings.SettingType;
import de.undefinedhuman.projectcreate.core.engine.settings.panels.PanelObject;
import de.undefinedhuman.projectcreate.core.engine.settings.types.BooleanSetting;
import de.undefinedhuman.projectcreate.core.engine.settings.types.TextureSetting;

public class SpriteLayer extends PanelObject {

    public Setting
            texture = new TextureSetting("Texture", "Unknown.png"),
            renderLevel = new Setting(SettingType.Int, "Render Level", 0),
            isAnimated = new BooleanSetting("Is Animated", true);

    public SpriteLayer() {
        settings.addSettings(texture, renderLevel, isAnimated);
    }

}
