package de.undefinedhuman.projectcreate.engine.entity.components.arm;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.entity.Component;
import de.undefinedhuman.projectcreate.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.types.Vector2Setting;

import java.util.HashMap;

public class RightArmBlueprint extends ComponentBlueprint {

    private Setting
            textureName = new Setting("Layer", ""),
            selectedTexture = new Setting("Selected Layer", ""),
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
