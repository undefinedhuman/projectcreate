package de.undefinedhuman.sandboxgame.entity.ecs.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.collision.CollisionManager;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.entity.components.animation.AnimationComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.collision.CollisionComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.movement.MovementComponent;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.engine.utils.math.Vector2i;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.System;
import de.undefinedhuman.sandboxgame.utils.Inputs;
import de.undefinedhuman.sandboxgame.world.WorldManager;

public class MovementSystem extends System {

    public static MovementSystem instance;

    private Vector2 currentVelocity = new Vector2(), currentPosition = new Vector2();

    public MovementSystem() {
        if (instance == null) instance = this;
    }

    // TODO Provide Collision for above World height and below 0
    // TODO Alle Blöcke an der Seite brauchen auch slopes also nicht nur der einzelne block nach oben sondern auch die zur seite brauchen slopes aber volle 45 Grad nicht wie oben da wenn man zur seite geht von vollen blöcken
    // TODO testen falls man voher nicht auf ner slope war und jetzt geht das kein block oberhalb blockiert

    @Override
    public void update(float delta, Entity entity) {

        MovementComponent movementComponent;
        CollisionComponent collisionComponent;
        AnimationComponent animationComponent;

        if ((movementComponent = (MovementComponent) entity.getComponent(ComponentType.MOVEMENT)) == null
                || (collisionComponent = (CollisionComponent) entity.getComponent(ComponentType.COLLISION)) == null
                || (animationComponent = (AnimationComponent) entity.getComponent(ComponentType.ANIMATION)) == null) return;

        currentVelocity.set(
                Gdx.input.getInputProcessor() == Inputs.instance ? movementComponent.getCurrentSpeed() : 0,
                movementComponent.velocity.y - movementComponent.getGravity());
        currentPosition.set(entity.getPosition());

        float velX = currentVelocity.x * delta;
        float velY = currentVelocity.y * delta;
        if(velX >= Variables.BLOCK_SIZE) velX = Variables.BLOCK_SIZE;
        if(velX <= -Variables.BLOCK_SIZE) velX = -Variables.BLOCK_SIZE;
        if(velY <= -Variables.BLOCK_SIZE) velY = -Variables.BLOCK_SIZE;

        Vector2 slopePosition = new Vector2().set(currentPosition);

        if(velY < 0) {

            collisionComponent.updateHitbox(slopePosition.add(velX, 0));
            boolean slopeRight = slopeCollision(movementComponent, collisionComponent.bottomRight(), velX);
            collisionComponent.updateHitbox(slopePosition.add(0, velY));
            boolean slopeBelow = slopeCollision(movementComponent, collisionComponent.bottomRight(), velX);

            if(!slopeRight && !slopeBelow && WorldManager.instance.isSlope(movementComponent.previousSlopeTile.x, movementComponent.previousSlopeTile.y)) {
                if (velX > 0) currentPosition.y = ((int) (slopePosition.y / Variables.BLOCK_SIZE) + 1) * Variables.BLOCK_SIZE;
                movementComponent.previousSlopeTile.set(-1, -1);
                movementComponent.isOnSlope = false;
                slopePosition.set(currentPosition).add(velX, 0);
                collisionComponent.updateHitbox(slopePosition);
                slopeCollision(movementComponent, collisionComponent.bottomRight(), 0);
            }

        }

        /*Vector2 slopeTilePosition;
        if(velY < 0) {
            if((slopeTilePosition = collideSlope(slopePosition.add(velX, 0))) != null) {
                currentPosition.x += velX;
                currentPosition.y = slopeTilePosition.y;
                currentVelocity.y = -1;
                movementComponent.canJump = true;
                movementComponent.previousSlopeTile.set(slopeTilePosition);
                isOnSlope = true;
            } else if((slopeTilePosition = collideSlope(slopePosition.add(0, velY))) != null) {
                currentPosition.x += velX;
                currentPosition.y = slopeTilePosition.y;
                currentVelocity.y = -1;
                movementComponent.canJump = true;
                movementComponent.previousSlopeTile.set(slopeTilePosition);
                isOnSlope = true;
            } else {

                if(WorldManager.instance.isSlope((int) movementComponent.previousSlopeTile.x, (int) movementComponent.previousSlopeTile.y)) {
                    currentPosition.y = ((int) ((slopePosition.y + velY) / Variables.BLOCK_SIZE)) * Variables.BLOCK_SIZE;
                    slopePosition.y = (int) (currentPosition.y + Variables.BLOCK_SIZE);

                    if((slopeTilePosition = collideSlope(slopePosition.add(velX, 0))) != null) {
                        currentPosition.x += velX;
                        currentPosition.y = slopeTilePosition.y;
                        currentVelocity.y = -1;
                        movementComponent.canJump = true;
                        movementComponent.previousSlopeTile.set(slopeTilePosition);
                        isOnSlope = true;
                    }

                }

                // movementComponent.previousSlopeTile.set(-1, -1);
            }
        }*/

        if(!movementComponent.isOnSlope) {
            collideVertical(collisionComponent, velX);
            collideHorizontal(collisionComponent, movementComponent, velY);
        }

        entity.setPosition(currentPosition);
        movementComponent.velocity = currentVelocity;

        animate(animationComponent, movementComponent);

    }

    private Vector2i collideSlope(Vector2 playerPixelPosition) {
        Vector2i blockPosition = new Vector2i(playerPixelPosition).div(Variables.BLOCK_SIZE);
        return WorldManager.instance.isSlope(blockPosition.x, blockPosition.y) ?
                blockPosition.mul(Variables.BLOCK_SIZE).add(0, (int) (playerPixelPosition.x % Variables.BLOCK_SIZE)) :
                null;
    }

    private boolean slopeCollision(MovementComponent movementComponent, Vector2 slopePosition, float velocityX) {
        Vector2i collisionResponse;
        if((collisionResponse = collideSlope(slopePosition)) == null) return false;
        currentPosition.x += velocityX;
        currentPosition.y = collisionResponse.y;
        currentVelocity.y = -1;
        movementComponent.setCanJump(true);
        movementComponent.isOnSlope = true;
        movementComponent.previousSlopeTile.set(collisionResponse.div(Variables.BLOCK_SIZE));
        return true;
    }

    private void collideVertical(CollisionComponent collisionComponent, float velocityX) {
        collisionComponent.updateHitbox(currentPosition.add(velocityX, 0));

        int collisionResponse;
        if(velocityX > 0 && (collisionResponse = CollisionManager.collideVer(collisionComponent.bottomRight(), collisionComponent.upperRight())) != Integer.MIN_VALUE)
            currentPosition.x = collisionResponse * Variables.BLOCK_SIZE - collisionComponent.getOffset().x - collisionComponent.getSize().x;
        else if(velocityX < 0 && (collisionResponse = CollisionManager.collideVer(collisionComponent.bottomLeft(), collisionComponent.upperLeft())) != Integer.MIN_VALUE)
            currentPosition.x = (collisionResponse + 1) * Variables.BLOCK_SIZE - collisionComponent.getOffset().x;
    }

    private void collideHorizontal(CollisionComponent collisionComponent, MovementComponent movementComponent, float velocityY) {
        collisionComponent.updateHitbox(currentPosition.add(0, velocityY));

        int collisionResponseValue;
        if (velocityY < 0 && (collisionResponseValue = CollisionManager.collideHor(collisionComponent.bottomLeft(), collisionComponent.bottomRight())) != Integer.MIN_VALUE) {
            currentPosition.y = (collisionResponseValue + 1) * Variables.BLOCK_SIZE - collisionComponent.getOffset().y;
            currentVelocity.y = -1;
            movementComponent.setCanJump(true);
        } else if(velocityY > 0 && (collisionResponseValue = CollisionManager.collideHor(collisionComponent.upperLeft(), collisionComponent.upperRight())) != Integer.MIN_VALUE) {
            currentPosition.y = (collisionResponseValue) * Variables.BLOCK_SIZE - collisionComponent.getOffset().y - collisionComponent.getSize().y;
            currentVelocity.y = 0;
        } else if(velocityY < 0)
            movementComponent.setCanJump(false);
    }

    private void animate(AnimationComponent animationComponent, MovementComponent movementComponent) {
        Vector2 velocity = movementComponent.velocity;
        if(movementComponent.isJumping && !movementComponent.isOnSlope)
            animationComponent.setAnimation(velocity.y > movementComponent.getJumpTans() ? "Jump" : (velocity.y < -movementComponent.getJumpTans() ? "Fall" : "Transition"));
        else animationComponent.setAnimation(velocity.x != 0 ? "Run" : "Idle");
    }

}
