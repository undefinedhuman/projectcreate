package de.undefinedhuman.projectcreate.server.entity;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.movement.MovementComponent;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.All;
import de.undefinedhuman.projectcreate.engine.ecs.systems.IteratingSystem;

@All({TransformComponent.class, MovementComponent.class})
public class MovementSystem extends IteratingSystem {

    private final Vector2 currentPosition = new Vector2();

     public MovementSystem() {
         super(5);
     }

    @Override
    public void processEntity(float delta, Entity entity) {
        TransformComponent transformComponent = Mappers.TRANSFORM.get(entity);
        MovementComponent movementComponent = Mappers.MOVEMENT.get(entity);

        movementComponent.velocity.x = movementComponent.getDirection() * movementComponent.getSpeed();
        movementComponent.velocity.y -= movementComponent.getGravity() * delta;

        currentPosition.set(moveEntity(transformComponent.getPosition(), movementComponent.velocity, delta));

        if(currentPosition.y <= 0) {
            currentPosition.y = 0;
            movementComponent.velocity.y = 0;
            movementComponent.setCanJump();
        }

        transformComponent.setPosition(currentPosition);
    }

    public static Vector2 moveEntity(Vector2 position, Vector2 velocity, float delta) {
         if(position == null)
             return new Vector2();
        return new Vector2(position).mulAdd(velocity, delta);
    }

}