package de.undefinedhuman.sandboxgameserver.entity.ecs.components.arm;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.file.FileReader;

public class LeftArmBlueprint extends ComponentBlueprint {

    private Vector2 turnedOffset, origin;

    public LeftArmBlueprint() {
        type = ComponentType.LEFTARM;

    }

    @Override
    public Component createInstance(Entity entity) {
        return new LeftArmComponent(entity);
    }

    @Override
    public void load(FileReader reader, int id) {
    }

    @Override
    public void delete() {
    }

}
