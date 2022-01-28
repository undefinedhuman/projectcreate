package de.undefinedhuman.projectcreate.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.undefinedhuman.projectcreate.core.ecs.EntityFlag;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.ecs.mouse.MouseComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.game.camera.CameraManager;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class MouseSystem extends IteratingSystem {

    public MouseSystem() {
        super(Family.all(TransformComponent.class, MouseComponent.class, SpriteComponent.class).get(), 0);
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        TransformComponent transformComponent = Mappers.TRANSFORM.get(entity);
        MouseComponent mouseComponent = Mappers.MOUSE.get(entity);
        SpriteComponent spriteComponent = Mappers.SPRITE.get(entity);

        if(EntityFlag.hasFlags(entity, EntityFlag.IS_MAIN_PLAYER)) mouseComponent.mousePosition.set(Tools.getMouseCoordsInWorldSpace(CameraManager.gameCamera));
        mouseComponent.isTurned = mouseComponent.mousePosition.x >= transformComponent.getCenterPosition().x;
        spriteComponent.setTurned(mouseComponent.isTurned);
    }

}
