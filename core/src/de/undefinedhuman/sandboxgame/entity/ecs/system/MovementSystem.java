package de.undefinedhuman.sandboxgame.entity.ecs.system;

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
import de.undefinedhuman.sandboxgame.utils.Tools;
import de.undefinedhuman.sandboxgame.world.WorldManager;

public class MovementSystem extends System {

    public static MovementSystem instance;

    private Vector2 currentPosition = new Vector2(), slopePosition = new Vector2();

    public MovementSystem() {
        if (instance == null) instance = this;
    }

    // TODO Eck Blöcke funkt. nicht so wirklich geil

    // TODO Implement Multiple collision checks for velocitys greater than player width

    // TODO Slope collision auf x achse nur für velX machen also wenn nach recht läuft mach das auch nur für die erste Slop collision halt von unten links nach oben rechts (boundsIndex = 5)
    // und für die zweite slope collision nach unten immer nur für die anderen slopes machen also falls oben mach den unteren check für (boundsIndex = 4)

    // TODO Für Blöcke bekommen nicht (int) sondern ceil verwenden dafür ecken als collision counten?
    // TODO Collision für horizontal unten ecken etc als collision werten, bzw. evtl für beide enden verschiedene Collision paare aufstel

    // TODO testen falls man voher nicht auf ner slope war und jetzt geht das kein block oberhalb blockiert

    @Override
    public void update(float delta, Entity entity) {

        MovementComponent movementComponent;
        CollisionComponent collisionComponent;
        AnimationComponent animationComponent;

        if ((movementComponent = (MovementComponent) entity.getComponent(ComponentType.MOVEMENT)) == null
                || (collisionComponent = (CollisionComponent) entity.getComponent(ComponentType.COLLISION)) == null
                || (animationComponent = (AnimationComponent) entity.getComponent(ComponentType.ANIMATION)) == null) return;

        // movementComponent.x += Gdx.input.getInputProcessor() == Inputs.instance ? movementComponent.getCurrentSpeed() : 0;

         //+ movementComponent.getDirection() * movementComponent.getSpeed() : movementComponent.velocity.x < 0 ? movementComponent.getSpeed() : -movementComponent.getSpeed();

         //if()

        movementComponent.velocity.x = movementComponent.getDirection() * movementComponent.getSpeed();

        //movementComponent.velocity.x += ((movementComponent.getDirection() * movementComponent.getSpeed()) - movementComponent.velocity.x) * (movementComponent.getDirection() == 0 ? 0.2f : 0.05f);
        if(movementComponent.getDirection() == 0 && Tools.isInRange(movementComponent.velocity.x, -5, 5)) movementComponent.velocity.x = 0;

        //movementComponent.velocity.x = Tools.clamp(movementComponent.velocity.x + (movementComponent.getDirection() != 0 ? movementComponent.getSpeed() : -movementComponent.getSpeed()) * 5f * delta, 0, movementComponent.getSpeed());
        movementComponent.velocity.y -= movementComponent.getGravity() * delta;

        animationComponent.setAnimationTimeMultiplier(movementComponent.velocity.x != 0 ? Math.abs(movementComponent.velocity.x) / movementComponent.getSpeed() : 1);

        currentPosition.set(entity.getPosition());

        float velX = movementComponent.velocity.x * delta;
        float velY = movementComponent.velocity.y * delta;
        if(velX > Variables.BLOCK_SIZE) velX = Variables.BLOCK_SIZE;
        if(velX < -Variables.BLOCK_SIZE) velX = -Variables.BLOCK_SIZE;
        if(velY < -Variables.BLOCK_SIZE) velY = -Variables.BLOCK_SIZE;

        collisionComponent.updateHitbox(new Vector2(currentPosition).add(velX, 0));

        // Check for upper horizontal collision when going on and while being on a slope, don't add x velocity when horizontal collision, wenn der gang frei ist der Spieler also verikal theoretisch auf die Slope drauf kann aber an die Decke stoßen würde

        if(movementComponent.rightSlope || CollisionManager.collideVer(collisionComponent.bottomRight(), collisionComponent.upperRight()) == CollisionManager.NO_COLLISION) {
            movementComponent.rightSlope = collisionSlopes(movementComponent, collisionComponent, velX, velY, velX > 0, 6, movementComponent.rightSlope, (byte) 4, (byte) 12);
        } else movementComponent.rightSlope = false;


        /*if(velY < 0) {

            collisionComponent.updateHitbox(currentPosition);

            movementComponent.leftSlope = collisionSlopes(movementComponent, collisionComponent, velX, velY, velX < 0, 4, movementComponent.leftSlope, (byte) 1, (byte) 9);
            movementComponent.middleSlope = collisionSlopes(movementComponent, collisionComponent, velX, velY, false, 5, movementComponent.middleSlope, (byte) 0, (byte) 8);
            movementComponent.rightSlope = collisionSlopes(movementComponent, collisionComponent, velX, velY, velX > 0, 6, movementComponent.rightSlope, (byte) 4, (byte) 12);

        } else {
            movementComponent.leftSlope = false;
            movementComponent.middleSlope = false;
            movementComponent.rightSlope = false;
        }*/

        //if(!movementComponent.leftSlope && !movementComponent.middleSlope && !movementComponent.rightSlope) {
        if(!movementComponent.rightSlope) {
            collideVertical(collisionComponent, velX);
            collideHorizontal(collisionComponent, movementComponent, velY);
        }
        //}

        entity.setPosition(currentPosition);

        animate(animationComponent, movementComponent);

    }

    private Vector2i collideSlope(Vector2 playerPixelPosition, byte[] slopes) {
        Vector2i blockPosition = new Vector2i(playerPixelPosition).div(Variables.BLOCK_SIZE);
        byte slopeHeight = WorldManager.instance.isSlope(playerPixelPosition.x, blockPosition.y, slopes);
        return slopeHeight != -1 ?
                blockPosition.mul(Variables.BLOCK_SIZE).add(0, slopeHeight) :
                null;
    }

    private boolean slopeCollision(CollisionComponent collisionComponent, MovementComponent movementComponent, Vector2 slopePosition, float velocityX, float velocityY, byte[] slopes) {
        Vector2i collisionResponse;
        if((collisionResponse = collideSlope(slopePosition, slopes)) == null) return false;
        currentPosition.x += velocityX;
        if(currentPosition.y > collisionResponse.y) {
            currentPosition.y += velocityY;
            return true;
        }
        currentPosition.y = collisionResponse.y;
        movementComponent.velocity.y = 0;
        movementComponent.setCanJump(true);
        return true;
    }

    private boolean newSlopeCollision(CollisionComponent collisionComponent, MovementComponent movementComponent, Vector2 slopePosition, float velocityX, float velocityY, byte[] slopes) {
        Vector2i collisionResponse;
        if((collisionResponse = collideSlope(slopePosition, slopes)) == null) return false;
        currentPosition.x += velocityX;
        currentPosition.y = collisionResponse.y;
        movementComponent.velocity.y = 0;
        movementComponent.setCanJump(true);
        return true;
    }

    private boolean collisionSlopes(MovementComponent movementComponent, CollisionComponent collisionComponent, float velX, float velY, boolean snap, int boundIndex, boolean isOnSlope, byte... slopes) {
        if(velY >= 0) return false;
        boolean currentIsOnSlope;
        if(!(currentIsOnSlope = slopeCollision(collisionComponent, movementComponent, collisionComponent.updateHitbox(slopePosition.set(currentPosition).add(velX, 0)).bound(boundIndex), velX, velY, slopes))
                && !(currentIsOnSlope = slopeCollision(collisionComponent, movementComponent, collisionComponent.updateHitbox(slopePosition.add(0, velY)).bound(boundIndex), velX, velY, slopes))
                && isOnSlope) {
            if (snap) currentPosition.y = ((int) (slopePosition.y / Variables.BLOCK_SIZE) + 1) * Variables.BLOCK_SIZE;
            //else currentPosition.y = ((int) (slopePosition.y / Variables.BLOCK_SIZE)) * Variables.BLOCK_SIZE;
            currentIsOnSlope = newSlopeCollision(collisionComponent, movementComponent, collisionComponent.updateHitbox(slopePosition.set(currentPosition).add(velX, 0)).bound(boundIndex), 0, 0, slopes);
        }
        return currentIsOnSlope;
    }

    private void collideVertical(CollisionComponent collisionComponent, float velocityX) {
        collisionComponent.updateHitbox(currentPosition.add(velocityX, 0));

        int collisionResponse;
        if(velocityX > 0 && (collisionResponse = CollisionManager.collideVer(collisionComponent.bottomRight(), collisionComponent.upperRight())) != Integer.MIN_VALUE) {
            currentPosition.x = collisionResponse * Variables.BLOCK_SIZE - collisionComponent.getOffset().x - collisionComponent.getSize().x;
        } else if(velocityX < 0 && (collisionResponse = CollisionManager.collideVer(collisionComponent.bottomLeft(), collisionComponent.upperLeft())) != Integer.MIN_VALUE) {
            currentPosition.x = (collisionResponse + 1) * Variables.BLOCK_SIZE - collisionComponent.getOffset().x;
        }
    }

    private void collideHorizontal(CollisionComponent collisionComponent, MovementComponent movementComponent, float velocityY) {
        collisionComponent.updateHitbox(currentPosition.add(0, velocityY));
        int collisionResponseValue;
        if (velocityY < 0 && (collisionResponseValue = CollisionManager.collideHor(collisionComponent.bottomLeft(), collisionComponent.bottomRight(), true)) != Integer.MIN_VALUE) {
            currentPosition.y = (collisionResponseValue + 1) * Variables.BLOCK_SIZE - collisionComponent.getOffset().y;
            movementComponent.velocity.y = -1;
            movementComponent.setCanJump(true);
        } else if(velocityY > 0 && (collisionResponseValue = CollisionManager.collideHor(collisionComponent.upperLeft(), collisionComponent.upperRight(), false)) != Integer.MIN_VALUE) {
            currentPosition.y = (collisionResponseValue) * Variables.BLOCK_SIZE - collisionComponent.getOffset().y - collisionComponent.getSize().y;
            movementComponent.velocity.y = 0;
        } else if(velocityY < 0)
            movementComponent.setCanJump(false);

        if(currentPosition.y <= 0) {
            currentPosition.y = 0; // TODO MAKE OUTER WORLD BLOCKS TO BEDROCK OR ANYTHING ELSE COLLIDABLE
            movementComponent.setCanJump(true);
            movementComponent.velocity.y = 0;
        }
    }

    private void animate(AnimationComponent animationComponent, MovementComponent movementComponent) {
        Vector2 velocity = movementComponent.velocity;
        if(movementComponent.isJumping && !movementComponent.leftSlope && !movementComponent.rightSlope)
            animationComponent.setAnimation(velocity.y > movementComponent.getJumpTans() ? "Jump" : (velocity.y < -movementComponent.getJumpTans() ? "Fall" : "Transition"));
        else animationComponent.setAnimation(velocity.x != 0 ? "Run" : "Idle");
    }

}
