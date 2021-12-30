package de.undefinedhuman.projectcreate.game.network.handler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.player.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.PositionPacket;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;

public class PositionPacketHandler implements PacketHandler {

    private Vector2 TEMP_POSITION = new Vector2();

    @Override
    public boolean handle(Connection connection, Packet packet) {
        if(!(packet instanceof PositionPacket)) return false;
        PositionPacket positionPacket = (PositionPacket) packet;
        Entity entity = EntityManager.getInstance().getEntity(positionPacket.getWorldID());
        if(entity == null || entity.isScheduledForRemoval() || entity.isRemoving()) return true;
        MovementComponent movementComponent = Mappers.MOVEMENT.get(entity);

        if(!(positionPacket.getTimeStamp() > movementComponent.latestPositionPacketTime))
            return true;
        movementComponent.latestPositionPacketTime = positionPacket.getTimeStamp();

        if(movementComponent.lastPositionPacketTimeLocal == 0)
            movementComponent.lastPositionPacketTimeLocal = System.nanoTime();

        if(entity != GameManager.getInstance().player) movementComponent.movementHistory.add(new MovementComponent.MovementFrame(positionPacket, (System.nanoTime() - movementComponent.lastPositionPacketTimeLocal) * 0.000000001f));
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

            movementComponent.predictedPosition = positionPacket.getPosition();
            for(MovementComponent.MovementFrame frame : movementComponent.movementHistory) {
                TEMP_POSITION.set(movementComponent.predictedPosition).mulAdd(frame.velocity, frame.delta);
                frame.position.set(TEMP_POSITION).sub(movementComponent.predictedPosition);
                movementComponent.predictedPosition.set(TEMP_POSITION);
            }

            if(movementComponent.predictedPosition.y <= 0) movementComponent.predictedPosition.y = 0;
        }
        movementComponent.lastPositionPacketTimeLocal = System.nanoTime();
        return true;
    }
}
