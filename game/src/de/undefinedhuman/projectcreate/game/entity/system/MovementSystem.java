package de.undefinedhuman.projectcreate.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.ecs.transform.TransformComponent;
import de.undefinedhuman.projectcreate.game.network.ClientManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class MovementSystem extends IteratingSystem {

    private static final float CONVERGE_MULTIPLIER = 0.025f;

    public MovementSystem() {
        super(Family.all(TransformComponent.class, MovementComponent.class).get(), 5);
    }

    private float lerp = 0;

    @Override
    protected void processEntity(Entity entity, float delta) {
        TransformComponent transformComponent = Mappers.TRANSFORM.get(entity);
        MovementComponent movementComponent = Mappers.MOVEMENT.get(entity);

        if(entity == GameManager.getInstance().player) {
            Vector2 position = moveEntity(movementComponent.predictedPosition, movementComponent.getDirection(), movementComponent.getSpeed(), delta);
            movementComponent.velocity.set(movementComponent.getDirection() * movementComponent.getSpeed() * delta, 0);

            MovementComponent.MovementFrame frame = new MovementComponent.MovementFrame();
            frame.delta = delta;
            frame.direction = movementComponent.getDirection();
            frame.position = new Vector2(position).sub(movementComponent.predictedPosition);
            frame.velocity = new Vector2(movementComponent.velocity);
            movementComponent.movementHistory.add(frame);
            movementComponent.historyLength += delta;

            float latency = Math.max(0.001f, ClientManager.getInstance().getLatency()) + 0.04f; // Math.max(0.001f, getLatency()) * 1000f;

            Vector2 extrapolatedPosition = new Vector2(movementComponent.predictedPosition).add(frame.velocity.x * latency * CONVERGE_MULTIPLIER, frame.velocity.y * latency * CONVERGE_MULTIPLIER);
            float t = delta / (latency * (1f + CONVERGE_MULTIPLIER));
            transformComponent.addPosition((extrapolatedPosition.x - transformComponent.getPosition().x) * t, (extrapolatedPosition.y - transformComponent.getPosition().y) * t);

            movementComponent.predictedPosition = position;
        } else {
            movementComponent.test += delta;
            transformComponent.setPosition(
                    Tools.lerp(movementComponent.lastPosition.x, movementComponent.predictedPosition.x, movementComponent.test / movementComponent.historyLength),
                    Tools.lerp(movementComponent.lastPosition.y, movementComponent.predictedPosition.y, movementComponent.test / movementComponent.historyLength)
            );
            /*if(movementComponent.movementHistory.size() <= 1)
                return;
            MovementComponent.MovementFrame frame1 = movementComponent.movementHistory.get(0);
            MovementComponent.MovementFrame frame2 = movementComponent.movementHistory.get(1);
            transformComponent.setPosition(Tools.lerp(frame1.position.x, frame2.position.x, delta / frame2.delta), Tools.lerp(frame1.position.y, frame2.position.y, delta / frame2.delta));
            frame2.delta -= delta;
            if(frame2.delta <= 0)
                movementComponent.movementHistory.remove(0);*/
        }
    }

    public static Vector2 moveEntity(Vector2 position, int direction, float speed, float delta) {
        if(position == null)
            return new Vector2();
        return new Vector2(position).add(direction * speed * delta, 0);
    }
}
