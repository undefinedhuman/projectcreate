package de.undefinedhuman.projectcreate.game.entity.system;

import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.movement.MovementComponent;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.All;
import de.undefinedhuman.projectcreate.engine.ecs.systems.IteratingSystem;
import de.undefinedhuman.projectcreate.game.camera.CameraManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;

@All({TransformComponent.class, MovementComponent.class})
public class CameraSystem extends IteratingSystem {

    public CameraSystem() {
        super(5);
    }

    @Override
    public void processEntity(float delta, Entity entity) {
        if(entity != GameManager.getInstance().player)
            return;
        CameraManager.getInstance().update(delta);
    }
}
