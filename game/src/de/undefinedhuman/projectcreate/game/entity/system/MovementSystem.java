package de.undefinedhuman.projectcreate.game.entity.system;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.animation.AnimationComponent;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.All;
import de.undefinedhuman.projectcreate.engine.ecs.systems.IteratingSystem;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.game.network.ClientManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.game.utils.Tools;

@All({TransformComponent.class, MovementComponent.class, AnimationComponent.class})
public class MovementSystem extends IteratingSystem {

    private static final float CONVERGE_MULTIPLIER = 0.05f;
    private static final float LATENCY_OFFSET = 60f/1000f-Variables.SERVER_TICK_RATE/1000f;
    private static final Vector2 EXTRAPOLATED_POSITION = new Vector2();

    public MovementSystem() {
        super(4);
    }

    @Override
    public void processEntity(float delta, Entity entity) {
        if(entity.isScheduledForRemoval())
            return;

        TransformComponent transformComponent = Mappers.TRANSFORM.get(entity);
        MovementComponent movementComponent = Mappers.MOVEMENT.get(entity);
        Vector2 velocity;

        if(entity == GameManager.getInstance().player) {

            movementComponent.velocity.x = movementComponent.getDirection() * movementComponent.getSpeed();
            movementComponent.velocity.y -= movementComponent.getGravity() * delta;

            Vector2 currentPosition = moveEntity(movementComponent.predictedPosition, movementComponent.velocity, delta);

            if(currentPosition.y <= 0) {
                movementComponent.velocity.y = 0f;
                currentPosition.y = 0;
                movementComponent.setCanJump();
            }

            MovementComponent.MovementFrame frame = new MovementComponent.MovementFrame(
                    currentPosition.x - movementComponent.predictedPosition.x,
                    currentPosition.y - movementComponent.predictedPosition.y,
                    movementComponent.velocity.x * delta,
                    movementComponent.velocity.y * delta,
                    delta
            );

            movementComponent.movementHistory.add(frame);
            movementComponent.historyLength += delta;

            float latency = (Math.max(0.001f, ClientManager.getInstance().getLatency()) + LATENCY_OFFSET) * (1f + CONVERGE_MULTIPLIER); // Math.max(0.001f, getLatency()) * 1000f;
            float t = delta / latency;
            EXTRAPOLATED_POSITION.set(movementComponent.predictedPosition).mulAdd(frame.velocity, latency).sub(transformComponent.getPosition()).scl(t);
            transformComponent.addPosition(EXTRAPOLATED_POSITION.x, EXTRAPOLATED_POSITION.y);

            movementComponent.predictedPosition = currentPosition;
            velocity = movementComponent.velocity;
        } else {

            // IF MOVEMENTHISTORY IS SMALLER THEN ONE FOR EXAMPLE IF LATENCY IS VERY BIG SUDDENLY, JUST INTERPOLATE MOVEMENT BASED ON VELOCITY FROM LAST FRAME
            if(movementComponent.movementHistory.size() <= 1) {
                movementComponent.delta = 0;
                return;
            }
            MovementComponent.MovementFrame current = movementComponent.movementHistory.get(0);
            MovementComponent.MovementFrame next = movementComponent.movementHistory.get(1);
            float f = movementComponent.delta / next.delta;
            transformComponent.setPosition(
                    Tools.lerp(current.position.x, next.position.x, f),
                    Tools.lerp(current.position.y, next.position.y, f)
            );
            movementComponent.delta += delta;
            if(movementComponent.delta >= next.delta) {
                movementComponent.movementHistory.remove(0);
                movementComponent.delta = movementComponent.delta - next.delta;
            }
            velocity = next.velocity;
        }

        animate(entity, movementComponent, velocity);
    }

    public static Vector2 moveEntity(Vector2 position, Vector2 velocity, float delta) {
        if(position == null)
            return new Vector2();
        return new Vector2(position).mulAdd(velocity, delta);
    }

    public static void animate(Entity entity, MovementComponent movementComponent, Vector2 velocity) {
        AnimationComponent animationComponent = Mappers.ANIMATION.get(entity);
        if(velocity.y != 0) animationComponent.setAnimation(velocity.y > 75f ? movementComponent.getJumpAnimation() : velocity.y < -75f ? movementComponent.getFallAnimation() : movementComponent.getTransitionAnimation());
        else animationComponent.setAnimation(velocity.x != 0 ? movementComponent.getRunAnimation() : movementComponent.getIdleAnimation());
    }

}
