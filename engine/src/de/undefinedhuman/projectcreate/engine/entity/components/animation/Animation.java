package de.undefinedhuman.projectcreate.engine.entity.components.animation;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;
import de.undefinedhuman.projectcreate.engine.settings.panels.PanelObject;
import de.undefinedhuman.projectcreate.engine.settings.types.SelectionSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.Vector2Setting;

public class Animation extends PanelObject {

    public Setting
            bounds = new Vector2Setting("Bounds", new Vector2()),
            frameTime = new Setting(SettingType.Float, "Frame Time", 0),
            playMode = new SelectionSetting("Play Mode", PlayMode.values());

    public Animation() {
        settings.addSettings(bounds, frameTime, playMode);
    }

    public int getSize() {
        return (int) (bounds.getVector2().y - bounds.getVector2().x + 1);
    }

}