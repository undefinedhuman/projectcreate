package de.undefinedhuman.projectcreate.server.network.handler;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.JumpPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.MovementPacket;
import de.undefinedhuman.projectcreate.core.network.packets.input.InputPacket;
import de.undefinedhuman.projectcreate.core.network.packets.input.InputPacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.input.responses.MousePacket;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.EntityManager;
import de.undefinedhuman.projectcreate.engine.file.FileUtils;
import de.undefinedhuman.projectcreate.engine.utils.Utils;
import de.undefinedhuman.projectcreate.server.ServerManager;
import de.undefinedhuman.projectcreate.server.entity.MovementSystem;
import de.undefinedhuman.projectcreate.server.network.PlayerConnection;
import de.undefinedhuman.projectcreate.server.network.session.SessionManager;

public class ServerInputPacketHandler extends InputPacketHandler {

    public ServerInputPacketHandler() {
        registerHandlerFunction(InputPacket.DIRECTION, this::handleDirection);
        registerHandlerFunction(InputPacket.JUMP, this::handleJump);
        registerHandlerFunction(InputPacket.MOUSE_POSITION, this::handleMouse);
    }

    private void handleDirection(Connection connection, InputPacket packet) {
        if(!(connection instanceof PlayerConnection) || ((PlayerConnection) connection).getDecryptionCipher() == null)
            return;
        PlayerConnection playerConnection = (PlayerConnection) connection;
        String[] data = InputPacket.parse(playerConnection.getDecryptionCipher(), packet).split(":");
        if(data.length != 2) return;
        Long worldID = SessionManager.getInstance().getWorldID(data[0]);
        final Integer direction;
        if (worldID == null || ((direction = Utils.isInteger(data[1])) == null || !Utils.isInRange(direction, -1, 1)))
            return;
        Entity player = EntityManager.getInstance().getEntity(worldID);
        if(player == null || player.isScheduledForRemoval()) return;
        ServerManager.getInstance().sendToAllExceptTCP(connection.getID(), MovementPacket.serialize(worldID, direction));
        long packetReceivedTime = System.nanoTime();
        ServerManager.getInstance().getBuffer().add(() -> {
            Entity entity = EntityManager.getInstance().getEntity(worldID);
            if(entity == null || entity.isScheduledForRemoval() || !connection.isConnected())
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

    private void handleJump(Connection connection, InputPacket packet) {
        if(!(connection instanceof PlayerConnection) || ((PlayerConnection) connection).getDecryptionCipher() == null)
            return;
        PlayerConnection playerConnection = (PlayerConnection) connection;
        String sessionID = InputPacket.parse(playerConnection.getDecryptionCipher(), packet);
        Long worldID = SessionManager.getInstance().getWorldID(sessionID);
        if (worldID == null) return;
        Entity player = EntityManager.getInstance().getEntity(worldID);
        if(player == null || player.isScheduledForRemoval()) return;
        ServerManager.getInstance().sendToAllExceptTCP(connection.getID(), JumpPacket.serialize(worldID));
        long packetReceivedTime = System.nanoTime();
        ServerManager.getInstance().getBuffer().add(() -> {
            Entity entity = EntityManager.getInstance().getEntity(worldID);
            if(entity == null || entity.isScheduledForRemoval() || !Mappers.MOVEMENT.has(entity) || !connection.isConnected())
                return;
            Mappers.MOVEMENT.get(entity).jump();
            float difference = (System.nanoTime() - packetReceivedTime) * 0.000000001f;
            float latency = connection.getReturnTripTime() * 0.0005f;
            float delta = difference + latency;
            TransformComponent transformComponent = Mappers.TRANSFORM.get(entity);
            MovementComponent movementComponent = Mappers.MOVEMENT.get(entity);
            movementComponent.velocity.y -= movementComponent.getGravity() * delta;
            transformComponent.addPosition(0, movementComponent.velocity.y * delta);
        });
    }

    private void handleMouse(Connection connection, InputPacket packet) {
        if(!(connection instanceof PlayerConnection) || ((PlayerConnection) connection).getDecryptionCipher() == null)
            return;
        PlayerConnection playerConnection = (PlayerConnection) connection;
        String[] data = InputPacket.parse(playerConnection.getDecryptionCipher(), packet).split(":");
        if(data.length != 6) return;
        Long worldID = SessionManager.getInstance().getWorldID(data[0]);
        if (worldID == null) return;
        Entity entity = EntityManager.getInstance().getEntity(worldID);
        if(entity == null || entity.isScheduledForRemoval() || !Mappers.MOUSE.has(entity)) return;
        Float mouseX, mouseY;
        boolean left = FileUtils.readBoolean(data[3]), right = FileUtils.readBoolean(data[4]), canShake = FileUtils.readBoolean(data[5]);
        if((mouseX = Utils.isFloat(data[1])) == null || (mouseY = Utils.isFloat(data[2])) == null) return;
        ServerManager.getInstance().sendToAllExceptTCP(connection.getID(), MousePacket.serialize(worldID, mouseX, mouseY, left, right, canShake));
        MousePacket.setMouseComponentData(Mappers.MOUSE.get(entity), mouseX, mouseY, left, right, canShake);
    }

}
