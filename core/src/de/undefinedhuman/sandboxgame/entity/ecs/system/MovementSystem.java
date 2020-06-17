package de.undefinedhuman.sandboxgame.entity.ecs.system;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.entity.components.animation.AnimationComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.collision.CollisionComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.movement.MovementComponent;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.System;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class MovementSystem extends System {

    public static MovementSystem instance;

    private Vector2 currentPosition = new Vector2();

    public MovementSystem() {
        if (instance == null) instance = this;
    }

    @Override
    public void update(float delta, Entity entity) {

        MovementComponent movementComponent;
        CollisionComponent collisionComponent;
        AnimationComponent animationComponent;

        if ((movementComponent = (MovementComponent) entity.getComponent(ComponentType.MOVEMENT)) == null
                || (collisionComponent = (CollisionComponent) entity.getComponent(ComponentType.COLLISION)) == null
                || (animationComponent = (AnimationComponent) entity.getComponent(ComponentType.ANIMATION)) == null) return;

        movementComponent.velocity.x += ((movementComponent.getDirection() * movementComponent.getSpeed()) - movementComponent.velocity.x) * 0.2f;
        if(movementComponent.getDirection() == 0 && Tools.isInRange(movementComponent.velocity.x, -5, 5)) movementComponent.velocity.x = 0;
        movementComponent.velocity.y -= movementComponent.getGravity() * delta;

        animationComponent.setAnimationTimeMultiplier(movementComponent.velocity.x != 0 ? Math.abs(movementComponent.velocity.x) / movementComponent.getSpeed() : 1);

        currentPosition.set(entity.getPosition());

        float velX = movementComponent.velocity.x * delta;
        float velY = movementComponent.velocity.y * delta;
        if(velX > Variables.BLOCK_SIZE) velX = Variables.BLOCK_SIZE;
        if(velX < -Variables.BLOCK_SIZE) velX = -Variables.BLOCK_SIZE;
        if(velY < -Variables.BLOCK_SIZE) velY = -Variables.BLOCK_SIZE;

        //collisionComponent.updateHitbox(new Vector2(currentPosition).add(velX, 0));

        // Check for upper horizontal collision when going on and while being on a slope, don't add x velocity when horizontal collision, wenn der gang frei ist der Spieler also verikal theoretisch auf die Slope drauf kann aber an die Decke stoßen würde


        /*
        if(movementComponent.rightSlope || CollisionManager.collideVer(collisionComponent.bottomRight(), collisionComponent.upperRight()) == CollisionManager.NO_COLLISION) {
            collisionComponent.updateHitbox(currentPosition);
            movementComponent.rightSlope = collisionSlopes(movementComponent, collisionComponent, velX, velY, velX > 0, 6, movementComponent.rightSlope, (byte) 4, (byte) 12);
        } else movementComponent.rightSlope = false;


        if(velY < 0) {

            collisionComponent.updateHitbox(currentPosition);

            movementComponent.leftSlope = collisionSlopes(movementComponent, collisionComponent, velX, velY, velX < 0, 4, movementComponent.leftSlope, (byte) 1, (byte) 9);
            movementComponent.middleSlope = collisionSlopes(movementComponent, collisionComponent, velX, velY, false, 5, movementComponent.middleSlope, (byte) 0, (byte) 8);
            movementComponent.rightSlope = collisionSlopes(movementComponent, collisionComponent, velX, velY, velX > 0, 6, movementComponent.rightSlope, (byte) 4, (byte) 12);

        } else {
            movementComponent.leftSlope = false;
            movementComponent.middleSlope = false;
            movementComponent.rightSlope = false;
        }

        //if(!movementComponent.leftSlope && !movementComponent.middleSlope && !movementComponent.rightSlope) {
        if(!movementComponent.rightSlope) {
            collideVertical(collisionComponent, velX);
            collideHorizontal(collisionComponent, movementComponent, velY);
        }*/
        //}

        entity.setPosition(currentPosition);

        animate(animationComponent, movementComponent);

    }

    private void animate(AnimationComponent animationComponent, MovementComponent movementComponent) {
        Vector2 velocity = movementComponent.velocity;
        if(movementComponent.isJumping && !movementComponent.leftSlope && !movementComponent.rightSlope)
            animationComponent.setAnimation(velocity.y > movementComponent.getJumpTans() ? "Jump" : (velocity.y < -movementComponent.getJumpTans() ? "Fall" : "Transition"));
        else animationComponent.setAnimation(velocity.x != 0 ? "Run" : "Idle");
    }

}
