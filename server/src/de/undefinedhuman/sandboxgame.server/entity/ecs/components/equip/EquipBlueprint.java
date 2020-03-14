package de.undefinedhuman.sandboxgameserver.entity.ecs.components.equip;

import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.file.FileReader;

public class EquipBlueprint extends ComponentBlueprint {

    public EquipBlueprint() {
        this.type = ComponentType.EQUIP;
    }

    @Override
    public Component createInstance(Entity entity) {
        return new EquipComponent(entity);
    }

    @Override
    public void load(FileReader reader, int id) { }

    @Override
    public void delete() { }

}
