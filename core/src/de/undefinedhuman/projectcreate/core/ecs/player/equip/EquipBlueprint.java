package de.undefinedhuman.projectcreate.core.ecs.player.equip;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.settings.types.TextureOffsetSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.StringSetting;

public class EquipBlueprint extends ComponentBlueprint {

    public StringSetting
            itemLayer = new StringSetting("Item Layer", "Item"),
            armLayer = new StringSetting("Arm Layer", "Right Arm"),
            hitboxLayer = new StringSetting("Hitbox Layer", "ItemHitbox");

    public TextureOffsetSetting
            itemPositions = new TextureOffsetSetting("Item Positions", new Vector2[0], false),
            itemOffsets = new TextureOffsetSetting("Item Offsets", new Vector2[0], true);

    public EquipBlueprint() {
        addSettings(itemLayer, armLayer, hitboxLayer, itemOffsets, itemPositions);
    }

    @Override
    public Component createInstance() {
        return new EquipComponent(itemOffsets.getValue(), itemPositions.getValue());
    }

}
