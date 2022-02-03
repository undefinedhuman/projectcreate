package de.undefinedhuman.projectcreate.game.network.handler;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.player.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.PositionPacket;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.EntityManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;

public class PositionPacketHandler implements PacketHandler<PositionPacket> {

    private Vector2 TEMP_POSITION = new Vector2();

    @Override
    public void handle(Connection connection, PositionPacket packet) {
        Entity entity = EntityManager.getInstance().getEntity(packet.getWorldID());
        if(entity == null || entity.isScheduledForRemoval()) return;
        MovementComponent movementComponent = Mappers.MOVEMENT.get(entity);

        if(!(packet.getTimeStamp() > movementComponent.latestPositionPacketTime))
            return;
        movementComponent.latestPositionPacketTime = packet.getTimeStamp();

        if(movementComponent.lastPositionPacketTimeLocal == 0)
            movementComponent.lastPositionPacketTimeLocal = System.nanoTime();

        if(entity != GameManager.getInstance().player) movementComponent.movementHistory.add(new MovementComponent.MovementFrame(packet, (System.nanoTime() - movementComponent.lastPositionPacketTimeLocal) * 0.000000001f));
        else {
            float dt = Math.max(0.001f, (System.nanoTime() - movementComponent.lastPositionPacketTimeLocal) * 0.000000001f);
            movementComponent.historyLength -= dt;

            while(movementComponent.movementHistory.size() > 0 && dt > 0) {
                MovementComponent.MovementFrame frame = movementComponent.movementHistory.get(0);

                if(dt >= frame.delta) {
                    dt -= frame.delta;
                    movementComponent.movementHistory.remove(0);
                } else {
                    float newDelta = 1 - dt / frame.delta;
                    frame.delta -= dt;
                    frame.position.scl(newDelta);
                    frame.velocity.scl(newDelta);
                    break;
                }

            }

            movementComponent.predictedPosition = packet.getPosition();
            for(MovementComponent.MovementFrame frame : movementComponent.movementHistory) {
                TEMP_POSITION.set(movementComponent.predictedPosition).mulAdd(frame.velocity, frame.delta);
                frame.position.set(TEMP_POSITION).sub(movementComponent.predictedPosition);
                movementComponent.predictedPosition.set(TEMP_POSITION);
            }

            if(movementComponent.predictedPosition.y <= 0) movementComponent.predictedPosition.y = 0;
        }
        movementComponent.lastPositionPacketTimeLocal = System.nanoTime();
    }
}
