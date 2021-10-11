package de.undefinedhuman.projectcreate.server.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.player.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformComponent;

public class MovementSystem extends IteratingSystem {

    private final Vector2 currentPosition = new Vector2();

     public MovementSystem() {
         super(Family.all(TransformComponent.class, MovementComponent.class).get(), 5);
     }

    @Override
    public void processEntity(Entity entity, float delta) {
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

            /*movementComponent.velocity.x += ((movementComponent.getDirection() * movementComponent.getSpeed()) - movementComponent.velocity.x) * 0.1f;
        if(movementComponent.getDirection() == 0 && Tools.isInRange(movementComponent.velocity.x, -5, 5)) movementComponent.velocity.x = 0;
        movementComponent.velocity.y -= movementComponent.getGravity() * deltaTime;

        animationComponent.setAnimationTimeMultiplier(movementComponent.velocity.x != 0 ? Math.abs(movementComponent.velocity.x) / movementComponent.getSpeed() : 1);

        currentPosition.set(transformComponent.getPosition());

        float velX = Tools.clamp(movementComponent.velocity.x * deltaTime, -Variables.COLLISION_SIZE, Variables.COLLISION_SIZE);
        float velY = Tools.clamp(movementComponent.velocity.y * deltaTime, -Variables.COLLISION_SIZE, Variables.COLLISION_SIZE);

        collisionComponent.update(currentPosition.add(velX, 0));
        Vector3 response = CollisionUtils.calculateCollisionX(collisionComponent);
        currentPosition.add(response.x, response.y);

        collisionComponent.update(currentPosition.add(0, velY));
        response = CollisionUtils.calculateCollisionY(collisionComponent);
        if(!response.isZero()) {
            if(velY < 0 && collisionComponent.onSlope && movementComponent.canJump && movementComponent.velocity.x != 0 && (velX>0) == (response.x>0)) movementComponent.velocity.y = Math.max(Math.min(movementComponent.velocity.y, -50), -500f);
            else movementComponent.velocity.y = 0;
            if(response.y > 0) movementComponent.canJump = true;
        }
        currentPosition.add(0, response.y);

        collisionComponent.update(currentPosition);
        currentPosition.add(0, CollisionUtils.calculateSlopeCollisionY(collisionComponent).y);
        collisionComponent.update(currentPosition);
        currentPosition.add(CollisionUtils.calculateSlopeCollisionX(collisionComponent).x, 0);*/

}