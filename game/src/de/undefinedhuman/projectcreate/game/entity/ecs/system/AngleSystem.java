package de.undefinedhuman.projectcreate.game.entity.ecs.system;

import de.undefinedhuman.projectcreate.core.ecs.angle.AngleComponent;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteData;
import de.undefinedhuman.projectcreate.game.camera.CameraManager;
import de.undefinedhuman.projectcreate.engine.ecs.system.System;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class AngleSystem extends System {

    @Override
    public void update(float delta, Entity entity) {
        AngleComponent angleComponent;
        SpriteComponent spriteComponent;

        if ((angleComponent = (AngleComponent) entity.getComponent(AngleComponent.class)) == null) return;

        if (entity.mainPlayer) {
            angleComponent.mousePos = Tools.getMouseCoordsInWorldSpace(CameraManager.gameCamera);
            boolean turned = angleComponent.mousePos.x < entity.getCenterPosition().x;
            angleComponent.angle = ((turned ? -1 : 1) * angleComponent.angle) % 360;
            angleComponent.isTurned = !turned;
        }

        if ((spriteComponent = (SpriteComponent) entity.getComponent(SpriteComponent.class)) == null) return;
        for (SpriteData data : spriteComponent.getSpriteData())
            data.setTurned(angleComponent.isTurned);
    }

}
