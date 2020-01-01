package de.undefinedhuman.sandboxgame.entity.ecs.components.equip;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentParam;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.HashMap;

public class EquipBlueprint extends ComponentBlueprint {

    private String itemTextureName, armTextureName, itemHitboxTextureName;
    private Vector2[] itemOffsets, itemPositions;
    private String[] invivisbleSprites;

    public EquipBlueprint() {

        itemTextureName = "";
        armTextureName = "";
        itemHitboxTextureName = "";
        itemOffsets = new Vector2[0];
        itemPositions = new Vector2[0];
        invivisbleSprites = new String[0];
        this.type = ComponentType.EQUIP;

    }

    @Override
    public Component createInstance(Entity entity, HashMap<ComponentType, ComponentParam> params) {
        return new EquipComponent(entity, itemTextureName, armTextureName, itemHitboxTextureName, itemOffsets, itemPositions, invivisbleSprites);
    }

    @Override
    public void load(HashMap<String, LineSplitter> settings, int id) {

        this.itemTextureName = Tools.loadString(settings,"Item Texture","Item");
        this.itemHitboxTextureName = Tools.loadString(settings,"Item Hitbox","ItemHitbox");
        this.armTextureName = Tools.loadString(settings,"Right Arm","RightArm");
        this.itemPositions = Tools.loadVector2Array(settings,"Item Position",null);
        this.itemOffsets = Tools.loadVector2Array(settings,"Item Offset",null);
        this.invivisbleSprites = Tools.loadStringArray(settings, "Invisible Sprites",null);

    }

    @Override
    public void delete() {}

}
