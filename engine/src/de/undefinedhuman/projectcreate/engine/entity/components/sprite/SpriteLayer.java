package de.undefinedhuman.projectcreate.engine.entity.components.sprite;

import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.panels.PanelObject;
import de.undefinedhuman.projectcreate.engine.settings.types.TextureSetting;

public class SpriteLayer extends PanelObject {

    public TextureSetting
            texture = new TextureSetting("Texture", "Unknown.png");

    public Setting
            renderLevel = new Setting("Render Level", 0),
            frameCount = new Setting("Frame Count", 1);

    public SpriteLayer() {
        settings.addSettings(texture, renderLevel, frameCount);
    }

}
