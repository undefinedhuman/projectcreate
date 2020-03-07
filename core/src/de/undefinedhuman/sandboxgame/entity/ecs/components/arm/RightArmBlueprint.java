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

public class RightArmBlueprint extends ComponentBlueprint {

    private String textureName, selectedTexture;
    private Vector2 turnedOffset, origin, shoulderPos;

    public RightArmBlueprint() {

        textureName = "";
        selectedTexture = "";
        turnedOffset = new Vector2(0, 0);
        origin = new Vector2(0, 0);
        shoulderPos = new Vector2(0, 0);
        type = ComponentType.RIGHTARM;

    }

    @Override
    public Component createInstance(Entity entity, HashMap<ComponentType, ComponentParam> params) {
        return new RightArmComponent(entity, textureName, turnedOffset, origin, shoulderPos, selectedTexture);
    }

    @Override
    public void load(HashMap<String, LineSplitter> settings, int id) {

        textureName = Tools.loadString(settings, "Texture Data", null);
        selectedTexture = Tools.loadString(settings, "Selected Texture Data", null);
        turnedOffset = Tools.loadVector2(settings, "Turned Offset", new Vector2());
        origin = Tools.loadVector2(settings, "Origin", new Vector2());
        shoulderPos = Tools.loadVector2(settings, "Shoulder Pos", new Vector2());

    }

    @Override
    public void delete() { }

}
