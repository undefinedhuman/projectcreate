package de.undefinedhuman.projectcreate.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.undefinedhuman.projectcreate.core.ecs.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.ecs.transform.TransformComponent;
import de.undefinedhuman.projectcreate.game.camera.CameraManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;

public class CameraSystem extends IteratingSystem {


    public CameraSystem() {
        super(Family.all(TransformComponent.class, MovementComponent.class).get(), 5);
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        if(entity != GameManager.getInstance().player)
            return;
        CameraManager.getInstance().update(delta);
    }
}
