package de.undefinedhuman.projectcreate.game.entity.ecs.system;

import de.undefinedhuman.projectcreate.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.engine.entity.components.mouse.AngleComponent;
import de.undefinedhuman.projectcreate.engine.entity.components.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.engine.entity.components.sprite.SpriteData;
import de.undefinedhuman.projectcreate.game.camera.CameraManager;
import de.undefinedhuman.projectcreate.game.entity.Entity;
import de.undefinedhuman.projectcreate.game.entity.ecs.System;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class AngleSystem extends System {

    @Override
    public void update(float delta, Entity entity) {
        AngleComponent angleComponent;
        SpriteComponent spriteComponent;

        if ((angleComponent = (AngleComponent) entity.getComponent(ComponentType.ANGLE)) == null) return;

        if (entity.mainPlayer) {
            angleComponent.mousePos = Tools.getMouseCoordsInWorldSpace(CameraManager.gameCamera);
            boolean turned = angleComponent.mousePos.x < entity.getCenterPosition().x;
            angleComponent.angle = ((turned ? -1 : 1) * angleComponent.angle) % 360;
            angleComponent.isTurned = !turned;
        }

        if ((spriteComponent = (SpriteComponent) entity.getComponent(ComponentType.SPRITE)) == null) return;
        for (SpriteData data : spriteComponent.getSpriteData())
            data.setTurned(angleComponent.isTurned);
    }

}
