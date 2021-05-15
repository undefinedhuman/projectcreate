package de.undefinedhuman.projectcreate.core.ecs.animation;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.settings.panels.PanelObject;
import de.undefinedhuman.projectcreate.engine.settings.types.SelectionSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.Vector2Setting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.FloatSetting;

public class Animation extends PanelObject {

    public Vector2Setting
            bounds = new Vector2Setting("Bounds", new Vector2());
    public FloatSetting
            frameTime = new FloatSetting("Frame Time", 0f);
    public SelectionSetting<PlayMode>
            playMode = new SelectionSetting<>("Play Mode", PlayMode.values(), value -> PlayMode.valueOf(String.valueOf(value)));

    public Animation() {
        settings.addSettings(bounds, frameTime, playMode);
    }

    public int getSize() {
        return (int) (bounds.getValue().y - bounds.getValue().x + 1);
    }

}
