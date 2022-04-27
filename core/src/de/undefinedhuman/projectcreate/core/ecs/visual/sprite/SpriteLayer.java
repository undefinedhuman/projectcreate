package de.undefinedhuman.projectcreate.core.ecs.visual.sprite;

import de.undefinedhuman.projectcreate.engine.settings.panels.PanelObject;
import de.undefinedhuman.projectcreate.engine.settings.types.TextureSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.BooleanSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

public class SpriteLayer extends PanelObject<String> {

    public TextureSetting
            texture = new TextureSetting("Texture", "Unknown.png");

    public IntSetting
            renderLevel = new IntSetting("Render Level", 0),
            frameCount = new IntSetting("Frame Count", 1);

    public BooleanSetting defaultVisibility = new BooleanSetting("Default Visibility", true);

    public SpriteLayer() {
        addSettings(texture, renderLevel, frameCount, defaultVisibility);
    }

    public SpriteData createSpriteData() {
        return new SpriteData(texture.getValue(), frameCount.getValue(), renderLevel.getValue(), defaultVisibility.getValue());
    }

}
