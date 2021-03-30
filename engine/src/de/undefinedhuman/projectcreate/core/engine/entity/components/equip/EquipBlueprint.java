package de.undefinedhuman.projectcreate.core.engine.entity.components.equip;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.engine.entity.Component;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.core.engine.settings.SettingType;
import de.undefinedhuman.projectcreate.core.engine.settings.types.StringArraySetting;
import de.undefinedhuman.projectcreate.core.engine.settings.types.TextureOffsetSetting;
import de.undefinedhuman.projectcreate.core.engine.settings.Setting;

import java.util.HashMap;

public class EquipBlueprint extends ComponentBlueprint {

    private Setting
            itemLayer = new Setting(SettingType.String, "Item Layer", "Item"),
            armLayer = new Setting(SettingType.String, "Arm Layer", "Right Arm"),
            hitboxLayer = new Setting(SettingType.String, "Hitbox Layer", "ItemHitbox"),
            invisibleLayers = new StringArraySetting("Invisible Layers", new String[0]),
            itemPositions = new TextureOffsetSetting("Item Positions", new Vector2[0], false),
            itemOffsets = new TextureOffsetSetting("Item Offsets", new Vector2[0], true);

    public EquipBlueprint() {
        settings.addSettings(itemLayer, armLayer, hitboxLayer, invisibleLayers, itemOffsets, itemPositions);
        this.type = ComponentType.EQUIP;

    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        return new EquipComponent(invisibleLayers.getStringArray(), itemOffsets.getVector2Array(), itemPositions.getVector2Array());
    }

    @Override
    public void delete() {}

}
