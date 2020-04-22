package de.undefinedhuman.sandboxgame.entity.ecs.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.collision.CollisionManager;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.entity.components.animation.AnimationComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.collision.CollisionComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.movement.MovementComponent;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.System;
import de.undefinedhuman.sandboxgame.utils.Inputs;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class MovementSystem extends System {

    public static MovementSystem instance;
    private Vector2 hitboxPosition = new Vector2(0, 0);
    private float jumpTrans = 75;

    public MovementSystem() {
        if (instance == null) instance = this;
    }

    @Override
    public void update(float delta, Entity entity) {

        MovementComponent movementComponent;
        CollisionComponent collisionComponent;
        AnimationComponent animationComponent;

        if ((movementComponent = (MovementComponent) entity.getComponent(ComponentType.MOVEMENT)) != null && (collisionComponent = (CollisionComponent) entity.getComponent(ComponentType.COLLISION)) != null && (animationComponent = (AnimationComponent) entity.getComponent(ComponentType.ANIMATION)) != null) {

            float currentX = entity.getPosition().x, currentY = entity.getPosition().y;
            boolean right = movementComponent.moveRight && Gdx.input.getInputProcessor() == Inputs.instance, left = movementComponent.moveLeft && Gdx.input.getInputProcessor() == Inputs.instance;

            Vector2 currentVel = movementComponent.velocity;
            currentVel.x = (left ? -1 : right ? 1 : 0) * movementComponent.getSpeed();

            currentX += currentVel.x * delta;
            updateHitBoxPos(new Vector2(currentX, currentY), collisionComponent);
            movementComponent.beginHike = CollisionManager.collideHike(hitboxPosition, collisionComponent.getSize());
            if(movementComponent.beginHike) currentY += movementComponent.getSpeed() * 1.5f * delta;

            updateHitBoxPos(new Vector2(currentX, currentY), collisionComponent);
            if (CollisionManager.collide(hitboxPosition, collisionComponent.getSize())) currentX = entity.getPosition().x;

            updateHitBoxPos(new Vector2(currentX, currentY), collisionComponent);
            currentVel.y -= movementComponent.getGravity() * delta;
            currentY += currentVel.y * delta;
            updateHitBoxPos(new Vector2(currentX, currentY), collisionComponent);

            if (CollisionManager.collide(hitboxPosition, collisionComponent.getSize()) || hitboxPosition.y <= 0) {
                currentY = Math.max(entity.getPosition().y, 0);
                currentVel.y = 0;
            }

            entity.setPosition(currentX, currentY);

            updateHitBoxPos(new Vector2(currentX, currentY - 4.0f), collisionComponent);
            movementComponent.setCanJump(CollisionManager.collide(hitboxPosition, collisionComponent.getSize()) && currentVel.y <= 0.0f);

            movementComponent.velocity = currentVel;

            if (currentVel.y > jumpTrans && !movementComponent.canJump) animationComponent.setAnimation("Jump");
            else if (Tools.isInRange(currentVel.y, -jumpTrans, jumpTrans) && !movementComponent.canJump)
                animationComponent.setAnimation("Transition");
            else if (currentVel.y < -jumpTrans && !movementComponent.canJump) animationComponent.setAnimation("Fall");
            else if (movementComponent.velocity.x != 0 || movementComponent.beginHike)
                animationComponent.setAnimation("Run");
            else animationComponent.setAnimation("Idle");

        }

    }

    private void updateHitBoxPos(Vector2 pos, CollisionComponent collisionComponent) {
        Vector2 position = new Vector2(pos.x + collisionComponent.getOffset().x, pos.y + collisionComponent.getOffset().y);
        hitboxPosition.set(position);
    }

}
