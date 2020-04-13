package de.undefinedhuman.sandboxgame.engine.entity.components.equip;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentParam;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.types.StringArraySetting;
import de.undefinedhuman.sandboxgame.engine.settings.types.TextureOffsetSetting;

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
        return new EquipComponent(itemLayer.getString(), armLayer.getString(), hitboxLayer.getString(), invisibleLayers.getStringArray(),  itemOffsets.getVector2Array(), itemPositions.getVector2Array());
    }

    @Override
    public void delete() {}

}
