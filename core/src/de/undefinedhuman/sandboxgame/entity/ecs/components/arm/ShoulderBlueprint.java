package de.undefinedhuman.sandboxgame.entity.ecs.components.arm;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentParam;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.HashMap;

public class ShoulderBlueprint extends ComponentBlueprint {

    private Vector2[] shoulderPos, shoulderOffsets;

    public ShoulderBlueprint() {

        this.shoulderPos = new Vector2[0];
        this.shoulderOffsets = new Vector2[0];

        this.type = ComponentType.SHOULDER;

    }

    @Override
    public Component createInstance(Entity entity, HashMap<ComponentType, ComponentParam> params) {
        return new ShoulderComponent(entity, shoulderPos, shoulderOffsets);
    }

    @Override
    public void load(HashMap<String, LineSplitter> settings, int id) {

        this.shoulderPos = Tools.loadVector2Array(settings,"Shoulder Pos",null);
        this.shoulderOffsets = Tools.loadVector2Array(settings,"Shoulder Off",null);

    }

    @Override
    public void delete() { }

}
