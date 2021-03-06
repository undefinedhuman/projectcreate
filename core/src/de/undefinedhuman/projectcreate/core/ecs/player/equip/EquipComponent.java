package de.undefinedhuman.projectcreate.core.ecs.player.equip;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

public class EquipComponent implements Component {

    private String defaultArmLayer, selectedArmLayer, itemLayer;
    private Vector2[] shoulderPositions, itemPositions, itemOffsets;

    public EquipComponent(
            String defaultArmLayer,
            String selectedArmLayer,
            String itemLayer,
            Vector2[] shoulderPositions,
            Vector2[] itemPositions,
            Vector2[] itemOffsets
    ) {
        this.defaultArmLayer = defaultArmLayer;
        this.selectedArmLayer = selectedArmLayer;
        this.itemLayer = itemLayer;
        this.shoulderPositions = shoulderPositions;
        this.itemPositions = itemPositions;
        this.itemOffsets = itemOffsets;
    }

    public String getDefaultArmLayer() {
        return defaultArmLayer;
    }

    public String getSelectedArmLayer() {
        return selectedArmLayer;
    }

    public String getItemLayer() {
        return itemLayer;
    }

    public Vector2 getShoulderPosition(int index) {
        if(!Utils.isInRange(index, 0, shoulderPositions.length-1))
            return new Vector2();
        return shoulderPositions[index];
    }

    public Vector2 getCurrentPosition(int index) {
        return itemPositions[index];
    }

    public Vector2 getWeaponOffset(int index) {
        if(!Utils.isInRange(index, 0, itemOffsets.length-1))
            return new Vector2();
        Vector2 vector = itemOffsets[index];
        return new Vector2(vector);
    }

}
