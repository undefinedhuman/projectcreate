package de.undefinedhuman.projectcreate.core.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.animation.AnimationComponent;
import de.undefinedhuman.projectcreate.core.ecs.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.ecs.transform.TransformComponent;

public abstract class MovementSystem extends IteratingSystem {

    private final Vector2 currentPosition = new Vector2();

    // BE CAREFULL WHEN ADDING ANIMATION BACK IN, SERVER SIDE MOVEMENT SYSTEM WONT UPDATE WHEN YOU ADD ANIMATION COMPONENT

    private boolean client;

     public MovementSystem(boolean client) {
         super(Family.all(TransformComponent.class, MovementComponent.class).get(), 5);
         this.client = client;
     }

    @Override
    public void processEntity(Entity entity, float delta) {
        TransformComponent transformComponent = Mappers.TRANSFORM.get(entity);
        MovementComponent movementComponent = Mappers.MOVEMENT.get(entity);

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

        if(client) {

            Vector2 position = moveEntity(movementComponent.predictedPosition, movementComponent.getDirection(), movementComponent.getSpeed(), delta);
            movementComponent.velocity.set(movementComponent.getDirection() * movementComponent.getSpeed() * delta, 0);

            MovementComponent.MovementFrame frame = new MovementComponent.MovementFrame();
            frame.delta = delta;
            frame.direction = movementComponent.getDirection();
            frame.position = new Vector2(position).sub(movementComponent.predictedPosition);
            frame.velocity = new Vector2(movementComponent.velocity);
            movementComponent.movementHistory.add(frame);
            movementComponent.historyLength += delta;

            float latency = Math.max(0.001f, getLatency()) + (60/1000f - 20f/1000f); // Math.max(0.001f, getLatency()) * 1000f;

            Vector2 extrapolatedPosition = new Vector2(movementComponent.predictedPosition).add(frame.velocity.x * latency * 0.025f, frame.velocity.y * latency * 0.025f);
            float t = delta / (latency * (1.025f));
            transformComponent.addPosition((extrapolatedPosition.x - transformComponent.getPosition().x) * t, (extrapolatedPosition.y - transformComponent.getPosition().y) * t);

            movementComponent.predictedPosition = position;
        } else {
            transformComponent.setPosition(moveEntity(transformComponent.getPosition(), movementComponent.getDirection(), movementComponent.getSpeed(), delta));
            movementComponent.velocity.set(movementComponent.getDirection() * movementComponent.getSpeed() * delta, 0);
        }

        // animate(animationComponent, movementComponent);
    }

    public static Vector2 moveEntity(Vector2 position, int direction, float speed, float delta) {
         if(position == null)
             return new Vector2();
        return new Vector2(position).add(direction * speed * delta, 0);
    }

    private void animate(AnimationComponent animationComponent, MovementComponent movementComponent) {
        Vector2 velocity = movementComponent.velocity;
        if(!movementComponent.canJump)
            animationComponent.setAnimation(velocity.y > movementComponent.getJumpTans() ? "Jump" : (velocity.y < -movementComponent.getJumpTans() ? "Fall" : "Transition"));
        else animationComponent.setAnimation(velocity.x != 0 ? "Run" : "Idle");
    }

    public abstract float getLatency();

}
