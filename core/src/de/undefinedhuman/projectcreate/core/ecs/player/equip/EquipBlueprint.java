package de.undefinedhuman.projectcreate.core.ecs.player.equip;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.settings.types.TextureOffsetSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.selection.DynamicSelectionSetting;

public class EquipBlueprint extends ComponentBlueprint {

    private DynamicSelectionSetting<String>
            defaultArmLayer = new DynamicSelectionSetting<>("Arm Layer", this::getSpriteLayerNames, value -> value, value -> value),
            selectedArmLayer = new DynamicSelectionSetting<>("Selected Arm Layer", this::getSpriteLayerNames, value -> value, value -> value),
            itemLayer = new DynamicSelectionSetting<>("Item Layer", this::getSpriteLayerNames, value -> value, value -> value);

    private TextureOffsetSetting
            shoulderPositions = new TextureOffsetSetting("Shoulder Positions", new Vector2[0], false),
            shoulderOffsets = new TextureOffsetSetting("Shoulder Offsets", new Vector2[0], true),
            itemPositions = new TextureOffsetSetting("Item Positions", new Vector2[0], false),
            itemOffsets = new TextureOffsetSetting("Item Offsets", new Vector2[0], true);

    public EquipBlueprint() {
        addSettings(defaultArmLayer, selectedArmLayer, itemLayer, shoulderPositions, shoulderOffsets, itemOffsets, itemPositions);
    }

    @Override
    public Component createInstance() {
        return new EquipComponent(defaultArmLayer.getValue(), selectedArmLayer.getValue(), itemLayer.getValue(), shoulderPositions.getValue(), shoulderOffsets.getValue(), itemPositions.getValue(), itemOffsets.getValue());
    }

    private String[] getSpriteLayerNames() {
        SpriteBlueprint blueprint = BlueprintManager.getInstance().getBlueprint(blueprintID).getComponentBlueprint(SpriteBlueprint.class);
        if(blueprint == null)
            return new String[0];
        return blueprint.spriteLayers.getValue().keySet().stream().sorted().toArray(String[]::new);
    }

}
