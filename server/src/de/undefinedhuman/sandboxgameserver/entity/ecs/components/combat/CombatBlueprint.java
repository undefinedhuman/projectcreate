package de.undefinedhuman.sandboxgameserver.entity.ecs.components.combat;

import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgameserver.file.FileReader;

public class CombatBlueprint extends ComponentBlueprint {

    public CombatBlueprint() {
    }

    @Override
    public Component createInstance(Entity entity) {
        return new CombatComponent(entity);
    }

    @Override
    public void load(FileReader reader, int id) {
    }

    @Override
    public void delete() {
    }

}
