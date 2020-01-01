package de.undefinedhuman.sandboxgame.entity.ecs.components.combat;

import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentParam;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

import java.util.HashMap;

public class CombatBlueprint extends ComponentBlueprint {

    public CombatBlueprint() {
    }

    @Override
    public Component createInstance(Entity entity, HashMap<ComponentType, ComponentParam> params) {
        return new CombatComponent(entity);
    }

    @Override
    public void load(HashMap<String, LineSplitter> settings, int id) {
        
    }

    @Override
    public void loadComponent(FileReader reader, int id) {
        super.loadComponent(reader, id);
    }

    @Override
    public void delete() {
    }

}
