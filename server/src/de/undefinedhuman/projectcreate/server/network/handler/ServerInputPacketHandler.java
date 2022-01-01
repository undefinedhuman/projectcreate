package de.undefinedhuman.projectcreate.server.network.handler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.MovementPacket;
import de.undefinedhuman.projectcreate.core.network.packets.input.InputPacket;
import de.undefinedhuman.projectcreate.core.network.packets.input.InputPacketHandler;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.engine.utils.Utils;
import de.undefinedhuman.projectcreate.server.ServerManager;
import de.undefinedhuman.projectcreate.server.entity.MovementSystem;
import de.undefinedhuman.projectcreate.server.network.PlayerConnection;
import de.undefinedhuman.projectcreate.server.network.session.SessionManager;

public class ServerInputPacketHandler extends InputPacketHandler {

    public ServerInputPacketHandler() {
        registerHandlerFunction(InputPacket.DIRECTION, this::handleDirection);
        registerHandlerFunction(InputPacket.JUMP, this::handleJump);
    }

    private void handleDirection(Connection connection, InputPacket packet) {
        if(!(connection instanceof PlayerConnection) || ((PlayerConnection) connection).getDecryptionCipher() == null)
            return;
        PlayerConnection playerConnection = (PlayerConnection) connection;
        String[] data = InputPacket.parse(playerConnection.getDecryptionCipher(), packet).split(":");
        Long worldID = SessionManager.getInstance().getWorldID(data[0]);
        final Integer direction;
        if (worldID == null || (direction = Utils.isInteger(data[1])) == null)
            return;
        direction = Utils.clamp(direction, -1, 1);
        Entity entity = EntityManager.getInstance().getEntity(worldID);
        if(entity == null || entity.isScheduledForRemoval() || entity.isRemoving()) return;
        ServerManager.getInstance().sendToAllExceptTCP(connection.getID(), MovementPacket.serialize(worldID, direction));
        long packetReceivedTime = System.nanoTime();
        ServerManager.getInstance().getBuffer().add(() -> {
            if(!EntityManager.getInstance().hasEntity(worldID) || !connection.isConnected())
                return;
            float difference = (System.nanoTime() - packetReceivedTime) * 0.000000001f;
            float latency = connection.getReturnTripTime() * 0.0005f;
            float delta = difference + latency;
            TransformComponent transformComponent = Mappers.TRANSFORM.get(entity);
            MovementComponent movementComponent = Mappers.MOVEMENT.get(entity);
            if(direction != 0) {
                transformComponent.setPosition(MovementSystem.moveEntity(transformComponent.getPosition(), new Vector2(direction * movementComponent.getSpeed(), 0), delta));
            } else {
                if(movementComponent.getDirection() != 0)
                    transformComponent.setPosition(MovementSystem.moveEntity(transformComponent.getPosition(), new Vector2(movementComponent.getDirection() * movementComponent.getSpeed(), 0), -1f * delta));
            }
            movementComponent.move(direction);
        });

    }

    private void handleJump(Connection connection, InputPacket packet) {}

}
