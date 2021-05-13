package de.undefinedhuman.projectcreate.core.ecs.equip;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentParam;
import de.undefinedhuman.projectcreate.engine.settings.types.StringArraySetting;
import de.undefinedhuman.projectcreate.engine.settings.types.TextureOffsetSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.StringSetting;

import java.util.HashMap;

public class EquipBlueprint extends ComponentBlueprint {

    public StringSetting
            itemLayer = new StringSetting("Item Layer", "Item"),
            armLayer = new StringSetting("Arm Layer", "Right Arm"),
            hitboxLayer = new StringSetting("Hitbox Layer", "ItemHitbox");

    public StringArraySetting
            invisibleLayers = new StringArraySetting("Invisible Layers", new String[0]);

    public TextureOffsetSetting
            itemPositions = new TextureOffsetSetting("Item Positions", new Vector2[0], false),
            itemOffsets = new TextureOffsetSetting("Item Offsets", new Vector2[0], true);

    public EquipBlueprint() {
        super(EquipComponent.class);
        settings.addSettings(itemLayer, armLayer, hitboxLayer, invisibleLayers, itemOffsets, itemPositions);

    }

    @Override
    public Component createInstance(HashMap<Class<? extends Component>, ComponentParam> params) {
        return new EquipComponent(invisibleLayers.getValue(), itemOffsets.getValue(), itemPositions.getValue());
    }

    @Override
    public void delete() {}

}
