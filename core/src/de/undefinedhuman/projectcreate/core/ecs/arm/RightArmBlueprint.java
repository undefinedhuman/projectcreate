package de.undefinedhuman.projectcreate.core.ecs.arm;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentParam;
import de.undefinedhuman.projectcreate.engine.settings.types.Vector2Setting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.StringSetting;

import java.util.HashMap;

public class RightArmBlueprint extends ComponentBlueprint {

    private StringSetting
            textureName = new StringSetting("Layer", ""),
            selectedTexture = new StringSetting("Selected Layer", "");
    private Vector2Setting
            turnedOffset = new Vector2Setting("Turned Offset", new Vector2()),
            origin = new Vector2Setting("Origin", new Vector2()),
            shoulderPosition = new Vector2Setting("Shoulder Position", new Vector2());

    public RightArmBlueprint() {
        super(RightArmComponent.class);
        settings.addSettings(textureName, selectedTexture, turnedOffset, origin, shoulderPosition);
    }

    @Override
    public Component createInstance(HashMap<Class<? extends Component>, ComponentParam> params) {
        return new RightArmComponent(textureName.getValue(), selectedTexture.getValue(), turnedOffset.getValue(), origin.getValue(), shoulderPosition.getValue());
    }

    @Override
    public void delete() { }

}
