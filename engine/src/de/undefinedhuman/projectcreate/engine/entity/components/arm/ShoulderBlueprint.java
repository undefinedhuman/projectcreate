package de.undefinedhuman.projectcreate.engine.entity.components.arm;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.engine.settings.types.TextureOffsetSetting;
import de.undefinedhuman.projectcreate.engine.entity.Component;
import de.undefinedhuman.projectcreate.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.engine.settings.Setting;

import java.util.HashMap;

public class ShoulderBlueprint extends ComponentBlueprint {

    private Setting
            shoulderPositions = new TextureOffsetSetting("Shoulder Positions", new Vector2[0], false),
            shoulderOffsets = new TextureOffsetSetting("Shoulder Offsets", new Vector2[0], true);

    public ShoulderBlueprint() {
        settings.addSettings(shoulderPositions, shoulderOffsets);
        this.type = ComponentType.SHOULDER;
    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        return new ShoulderComponent(shoulderPositions.getVector2Array(), shoulderOffsets.getVector2Array());
    }

    @Override
    public void delete() { }

}
