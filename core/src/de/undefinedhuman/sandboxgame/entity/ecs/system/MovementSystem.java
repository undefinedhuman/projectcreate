package de.undefinedhuman.sandboxgame.entity.ecs.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.collision.CollisionManager;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.entity.ecs.System;
import de.undefinedhuman.sandboxgame.entity.ecs.components.animation.AnimationComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.collision.CollisionComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.movement.MovementComponent;
import de.undefinedhuman.sandboxgame.utils.Inputs;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class MovementSystem extends System {

    public static MovementSystem instance;
    private Vector2 hitboxPosition = new Vector2(0, 0);
    private float jumpTrans = 75;

    @Override
    public void init(Entity entity) { }

    @Override
    public void update(float delta, Entity entity) {

        MovementComponent movementComponent;
        CollisionComponent collisionComponent;
        AnimationComponent animationComponent;

        if ((movementComponent = (MovementComponent) entity.getComponent(ComponentType.MOVEMENT)) != null && (collisionComponent = (CollisionComponent) entity.getComponent(ComponentType.COLLISION)) != null && (animationComponent = (AnimationComponent) entity.getComponent(ComponentType.ANIMATION)) != null) {

            float currentX = entity.transform.getPosition().x, currentY = entity.transform.getPosition().y;
            boolean right = movementComponent.moveRight && Gdx.input.getInputProcessor() == Inputs.instance, left = movementComponent.moveLeft && Gdx.input.getInputProcessor() == Inputs.instance;

            Vector2 currentVel = movementComponent.velocity;
            if (left) {
                currentVel.x = -movementComponent.getSpeed();
            } else if (right) {
                currentVel.x = movementComponent.getSpeed();
            } else {
                currentVel.x = 0.0f;
            }

            currentX += currentVel.x * delta;
            updateHitBoxPos(new Vector2(currentX, currentY), collisionComponent);
            if (CollisionManager.collideHike(hitboxPosition, collisionComponent.getWidth(), collisionComponent.getHeight())) {

                currentY += movementComponent.getSpeed() * 3.0F / 2.0F * delta;
                movementComponent.beginHike = true;

            } else {
                movementComponent.beginHike = false;
            }

            updateHitBoxPos(new Vector2(currentX, currentY), collisionComponent);
            if (CollisionManager.collide(hitboxPosition, collisionComponent.getWidth(), collisionComponent.getHeight())) {
                currentX = entity.transform.getPosition().x;
            }

            updateHitBoxPos(new Vector2(currentX, currentY), collisionComponent);
            currentVel.y -= movementComponent.getGravity() * delta;
            currentY += currentVel.y * delta;
            updateHitBoxPos(new Vector2(currentX, currentY), collisionComponent);

            if (CollisionManager.collide(hitboxPosition, collisionComponent.getWidth(), collisionComponent.getHeight()) || hitboxPosition.y <= 0) {
                currentY = Math.max(entity.transform.getPosition().y, 0);
                currentVel.y = 0;
            }

            entity.transform.setPosition(currentX, currentY);

            updateHitBoxPos(new Vector2(currentX, currentY - 4.0f), collisionComponent);
            movementComponent.setCanJump(CollisionManager.collide(hitboxPosition, collisionComponent.getWidth(), collisionComponent.getHeight()) && currentVel.y <= 0.0f);

            movementComponent.velocity = currentVel;

            if (currentVel.y > jumpTrans && !movementComponent.canJump) animationComponent.setAnimation("JUMP");
            else if (Tools.isInRange(currentVel.y, -jumpTrans, jumpTrans) && !movementComponent.canJump)
                animationComponent.setAnimation("TRANSITION");
            else if (currentVel.y < -jumpTrans && !movementComponent.canJump) animationComponent.setAnimation("FALL");
            else if (movementComponent.velocity.x != 0 || movementComponent.beginHike)
                animationComponent.setAnimation("RUN");
            else animationComponent.setAnimation("IDLE");

        }

    }

    @Override
    public void render(SpriteBatch batch) {}

    private void updateHitBoxPos(Vector2 pos, CollisionComponent collisionComponent) {

        Vector2 position = new Vector2(pos.x + collisionComponent.getOffset().x, pos.y + collisionComponent.getOffset().y);
        hitboxPosition.set(position);

    }

}
