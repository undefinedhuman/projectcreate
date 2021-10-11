package de.undefinedhuman.projectcreate.core.ecs.player.shoulder;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.settings.types.TextureOffsetSetting;

public class ShoulderBlueprint extends ComponentBlueprint {

    private TextureOffsetSetting
            shoulderPositions = new TextureOffsetSetting("Shoulder Positions", new Vector2[0], false),
            shoulderOffsets = new TextureOffsetSetting("Shoulder Offsets", new Vector2[0], true);

    public ShoulderBlueprint() {
        addSettings(shoulderPositions, shoulderOffsets);
    }

    @Override
    public Component createInstance() {
        return new ShoulderComponent(shoulderPositions.getValue(), shoulderOffsets.getValue());
    }

    @Override
    public void delete() { }

}
