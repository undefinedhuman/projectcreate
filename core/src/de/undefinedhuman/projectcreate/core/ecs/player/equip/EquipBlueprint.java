package de.undefinedhuman.projectcreate.core.ecs.player.equip;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.settings.types.TextureOffsetSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.selection.DynamicStringSelectionSetting;

public class EquipBlueprint extends ComponentBlueprint {

    private DynamicStringSelectionSetting
            defaultArmLayer = new DynamicStringSelectionSetting("Arm Layer", this::getSpriteLayerNames),
            selectedArmLayer = new DynamicStringSelectionSetting("Selected Arm Layer", this::getSpriteLayerNames),
            itemLayer = new DynamicStringSelectionSetting("Item Layer", this::getSpriteLayerNames);

    private TextureOffsetSetting
            shoulderPositions = new TextureOffsetSetting("Shoulder Positions", new Vector2[0], false),
            itemPositions = new TextureOffsetSetting("Item Positions", new Vector2[0], false),
            itemOffsets = new TextureOffsetSetting("Item Offsets", new Vector2[0], true);

    public EquipBlueprint() {
        addSettings(defaultArmLayer, selectedArmLayer, itemLayer, shoulderPositions, itemOffsets, itemPositions);
    }

    @Override
    public Component createInstance() {
        return new EquipComponent(defaultArmLayer.getValue(), selectedArmLayer.getValue(), itemLayer.getValue(), shoulderPositions.getValue(), itemPositions.getValue(), itemOffsets.getValue());
    }

    private String[] getSpriteLayerNames() {
        SpriteBlueprint blueprint = BlueprintManager.getInstance().getBlueprint(blueprintID).getComponentBlueprint(SpriteBlueprint.class);
        if(blueprint == null)
            return new String[0];
        return blueprint.spriteLayers.getValue().keySet().stream().sorted().toArray(String[]::new);
    }

}
