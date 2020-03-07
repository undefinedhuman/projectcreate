package de.undefinedhuman.sandboxgame.entity.ecs.components.name;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentParam;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.HashMap;

public class NameBlueprint extends ComponentBlueprint {

    private String name;

    public NameBlueprint() {

        this.name = "";
        this.type = ComponentType.NAME;

    }

    @Override
    public Component createInstance(Entity entity, HashMap<ComponentType, ComponentParam> params) {

        if (params.containsKey(ComponentType.NAME)) this.name = ((NameParam) params.get(ComponentType.NAME)).getName();
        return new NameComponent(entity, name);

    }

    @Override
    public void load(HashMap<String, LineSplitter> settings, int id) {

        this.name = Tools.loadString(settings, "Name", "");

    }

    @Override
    public void delete() {}

}
