package de.undefinedhuman.sandboxgame.engine.entity.components.sprite;

import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.panels.PanelObject;
import de.undefinedhuman.sandboxgame.engine.settings.types.BooleanSetting;
import de.undefinedhuman.sandboxgame.engine.settings.types.TextureSetting;

public class SpriteLayer extends PanelObject {

    public Setting
            texture = new TextureSetting("Texture", "Unknown.png"),
            renderLevel = new Setting(SettingType.Int, "Render Level", 0),
            isAnimated = new BooleanSetting("Is Animated", true);

    public SpriteLayer() {
        settings.addSettings(texture, renderLevel, isAnimated);
    }

}
