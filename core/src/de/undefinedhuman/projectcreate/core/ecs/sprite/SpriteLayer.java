package de.undefinedhuman.projectcreate.core.ecs.sprite;

import de.undefinedhuman.projectcreate.engine.settings.panels.PanelObject;
import de.undefinedhuman.projectcreate.engine.settings.types.TextureSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

public class SpriteLayer extends PanelObject {

    public TextureSetting
            texture = new TextureSetting("Texture", "Unknown.png");

    public IntSetting
            renderLevel = new IntSetting("Render Level", 0),
            frameCount = new IntSetting("Frame Count", 1);

    public SpriteLayer() {
        addSettings(texture, renderLevel, frameCount);
    }

}
