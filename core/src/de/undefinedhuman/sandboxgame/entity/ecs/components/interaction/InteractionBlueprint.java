package de.undefinedhuman.sandboxgame.entity.ecs.components.interaction;

import com.badlogic.gdx.Input;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentParam;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.HashMap;

public class InteractionBlueprint extends ComponentBlueprint {

    private int range = 0, key = Input.Keys.ANY_KEY;

    public InteractionBlueprint() {
        this.type = ComponentType.INTERACTION;
    }

    @Override
    public Component createInstance(Entity entity, HashMap<ComponentType, ComponentParam> params) {
        return new InteractionComponent(entity, range, key);
    }

    @Override
    public void load(HashMap<String, LineSplitter> settings, int id) {
        this.range = Tools.loadInt(settings,"Range",0);
        this.key = Tools.loadInt(settings,"Key", Input.Keys.F);
    }

    @Override
    public void delete() {}

}
