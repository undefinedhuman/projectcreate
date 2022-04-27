package de.undefinedhuman.projectcreate.game.entity.system;

import de.undefinedhuman.projectcreate.core.ecs.EntityFlag;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.ecs.mouse.MouseComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.All;
import de.undefinedhuman.projectcreate.engine.ecs.systems.IteratingSystem;
import de.undefinedhuman.projectcreate.game.camera.CameraManager;
import de.undefinedhuman.projectcreate.game.utils.Tools;

@All({TransformComponent.class, MouseComponent.class, SpriteComponent.class})
public class MouseSystem extends IteratingSystem {

    public MouseSystem() {}

    @Override
    public void processEntity(float delta, Entity entity) {
        TransformComponent transformComponent = Mappers.TRANSFORM.get(entity);
        MouseComponent mouseComponent = Mappers.MOUSE.get(entity);
        SpriteComponent spriteComponent = Mappers.SPRITE.get(entity);

        if(EntityFlag.hasFlags(entity, EntityFlag.MAIN_PLAYER)) mouseComponent.mousePosition.set(Tools.getMouseCoordsInWorldSpace(CameraManager.gameCamera));
        mouseComponent.isTurned = mouseComponent.mousePosition.x >= transformComponent.getCenterPosition().x;
        spriteComponent.setTurned(mouseComponent.isTurned);
    }

}
