package de.undefinedhuman.sandboxgame.engine.entity.components.arm;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentParam;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.types.Vector2Setting;

import java.util.HashMap;

public class RightArmBlueprint extends ComponentBlueprint {

    private Setting
            textureName = new Setting(SettingType.String, "Layer", ""),
            selectedTexture = new Setting(SettingType.String, "Selected Layer", ""),
            turnedOffset = new Vector2Setting("Turned Offset", new Vector2()),
            origin = new Vector2Setting("Origin", new Vector2()),
            shoulderPosition = new Vector2Setting("Shoulder Position", new Vector2());

    public RightArmBlueprint() {
        settings.addSettings(textureName, selectedTexture, turnedOffset, origin, shoulderPosition);
        type = ComponentType.RIGHTARM;
    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        return new RightArmComponent(textureName.getString(), selectedTexture.getString(), turnedOffset.getVector2(), origin.getVector2(), shoulderPosition.getVector2());
    }

    @Override
    public void delete() { }

}
