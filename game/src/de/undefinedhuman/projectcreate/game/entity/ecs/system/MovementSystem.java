package de.undefinedhuman.projectcreate.game.entity.ecs.system;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.undefinedhuman.projectcreate.core.ecs.animation.AnimationComponent;
import de.undefinedhuman.projectcreate.core.ecs.collision.CollisionComponent;
import de.undefinedhuman.projectcreate.core.ecs.movement.MovementComponent;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.game.entity.Entity;
import de.undefinedhuman.projectcreate.game.entity.ecs.System;
import de.undefinedhuman.projectcreate.game.screen.CollisionUtils;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class MovementSystem extends System {

    private Vector2 currentPosition = new Vector2();

    @Override
    public void update(float delta, Entity entity) {
        MovementComponent movementComponent;
        CollisionComponent collisionComponent;
        AnimationComponent animationComponent;

        if ((movementComponent = (MovementComponent) entity.getComponent(MovementComponent.class)) == null
                || (collisionComponent = (CollisionComponent) entity.getComponent(CollisionComponent.class)) == null
                || (animationComponent = (AnimationComponent) entity.getComponent(AnimationComponent.class)) == null) return;

        movementComponent.velocity.x += ((movementComponent.getDirection() * movementComponent.getSpeed()) - movementComponent.velocity.x) * 0.1f;
        if(movementComponent.getDirection() == 0 && Tools.isInRange(movementComponent.velocity.x, -5, 5)) movementComponent.velocity.x = 0;
        movementComponent.velocity.y -= movementComponent.getGravity() * delta;

        animationComponent.setAnimationTimeMultiplier(movementComponent.velocity.x != 0 ? Math.abs(movementComponent.velocity.x) / movementComponent.getSpeed() : 1);

        currentPosition.set(entity.getPosition());

        float velX = Tools.clamp(movementComponent.velocity.x * delta, -Variables.COLLISION_SIZE, Variables.COLLISION_SIZE);
        float velY = Tools.clamp(movementComponent.velocity.y * delta, -Variables.COLLISION_SIZE, Variables.COLLISION_SIZE);

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
        currentPosition.add(CollisionUtils.calculateSlopeCollisionX(collisionComponent).x, 0);

        entity.setPosition(currentPosition);

        animate(animationComponent, movementComponent);
    }

    private void animate(AnimationComponent animationComponent, MovementComponent movementComponent) {
        Vector2 velocity = movementComponent.velocity;
        if(!movementComponent.canJump)
            animationComponent.setAnimation(velocity.y > movementComponent.getJumpTans() ? "Jump" : (velocity.y < -movementComponent.getJumpTans() ? "Fall" : "Transition"));
        else animationComponent.setAnimation(velocity.x != 0 ? "Run" : "Idle");
    }

}
