package de.undefinedhuman.projectcreate.core.ecs.arm;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentParam;
import de.undefinedhuman.projectcreate.engine.settings.types.TextureOffsetSetting;

import java.util.HashMap;

public class ShoulderBlueprint extends ComponentBlueprint {

    private TextureOffsetSetting
            shoulderPositions = new TextureOffsetSetting("Shoulder Positions", new Vector2[0], false),
            shoulderOffsets = new TextureOffsetSetting("Shoulder Offsets", new Vector2[0], true);

    public ShoulderBlueprint() {
        super(ShoulderComponent.class);
        settings.addSettings(shoulderPositions, shoulderOffsets);
    }

    @Override
    public Component createInstance(HashMap<Class<? extends Component>, ComponentParam> params) {
        return new ShoulderComponent(shoulderPositions.getValue(), shoulderOffsets.getValue());
    }

    @Override
    public void delete() { }

}
