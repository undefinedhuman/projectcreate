package de.undefinedhuman.sandboxgame.entity.ecs.components.stats.mana;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentParam;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.HashMap;

public class ManaBlueprint extends ComponentBlueprint {

    private float maxMana;

    public ManaBlueprint() {

        this.maxMana = 0;
        this.type = ComponentType.MANA;

    }

    @Override
    public Component createInstance(Entity entity, HashMap<ComponentType, ComponentParam> params) {

        return new ManaComponent(entity, maxMana);

    }

    @Override
    public void load(HashMap<String, LineSplitter> settings, int id) {

        this.maxMana = Tools.loadFloat(settings, "MaxMana", 0);

    }

    @Override
    public void delete() {}

}
