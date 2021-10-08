package de.undefinedhuman.projectcreate.server.network;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.ecs.name.NameComponent;
import de.undefinedhuman.projectcreate.core.ecs.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.LoginPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.CreateEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.ComponentPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.JumpPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.MovementPacket;
import de.undefinedhuman.projectcreate.core.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.server.ServerManager;
import de.undefinedhuman.projectcreate.server.entity.MovementSystem;
import de.undefinedhuman.projectcreate.server.utils.IDManager;

import java.util.Map;

public class ServerPacketHandler implements PacketHandler {
    @Override
    public void handle(Connection connection, LoginPacket packet) {
        long worldID = IDManager.getInstance().createNewID();
        Entity player = BlueprintManager.getInstance().createEntity(BlueprintManager.PLAYER_BLUEPRINT_ID, worldID);
        NameComponent nameComponent = Mappers.NAME.get(player);
        if(nameComponent != null)
            nameComponent.setName(packet.name);
        ServerManager.getInstance().sendToAllExceptTCP(connection.getID(), CreateEntityPacket.serialize(player));
        packet.worldID = worldID;
        packet.componentData = PacketUtils.createComponentData(player.getComponents());
        connection.sendTCP(packet);
        EntityManager.getInstance().stream().map(Map.Entry::getValue).forEach(entity -> connection.sendTCP(CreateEntityPacket.serialize(entity)));
        EntityManager.getInstance().addEntity(worldID, player);
        ((PlayerConnection) connection).worldID = worldID;
    }

    @Override
    public void handle(Connection connection, ComponentPacket packet) {
        Entity entity = EntityManager.getInstance().getEntity(packet.worldID);
        if(entity == null || entity.isScheduledForRemoval() || entity.isRemoving()) return;
        ComponentPacket.parse(entity, packet);
        ServerManager.getInstance().sendToAllExceptTCP(connection.getID(), packet);
    }

    @Override
    public void handle(Connection connection, MovementPacket packet) {
        Entity entity = EntityManager.getInstance().getEntity(packet.worldID);
        if(entity == null || entity.isScheduledForRemoval() || entity.isRemoving()) return;
        ServerManager.getInstance().sendToAllExceptTCP(connection.getID(), packet);
        long packetReceivedTime = System.nanoTime();
        ServerManager.getInstance().COMMAND_CACHE.add(() -> {
            float difference = (System.nanoTime() - packetReceivedTime) * 0.000000001f;
            float latency = connection.getReturnTripTime() * 0.0005f;
            float delta = difference + latency;
            TransformComponent transformComponent = Mappers.TRANSFORM.get(entity);
            MovementComponent movementComponent = Mappers.MOVEMENT.get(entity);
            if(packet.direction != 0) {
                transformComponent.setPosition(MovementSystem.moveEntity(transformComponent.getPosition(), new Vector2(packet.direction * movementComponent.getSpeed(), 0), delta));
            } else {
                if(movementComponent.getDirection() != 0)
                    transformComponent.setPosition(MovementSystem.moveEntity(transformComponent.getPosition(), new Vector2(movementComponent.getDirection() * movementComponent.getSpeed(), 0), -1f * delta));
            }
            MovementPacket.parse(entity, packet);
        });
    }

    @Override
    public void handle(Connection connection, JumpPacket packet) {
        Entity entity = EntityManager.getInstance().getEntity(((PlayerConnection) connection).worldID);
        if(entity == null) return;
        packet.worldID = ((PlayerConnection) connection).worldID;
        ServerManager.getInstance().sendToAllExceptTCP(connection.getID(), packet);
        long packetReceivedTime = System.nanoTime();
        ServerManager.getInstance().COMMAND_CACHE.add(() -> {
            JumpPacket.parse(entity, packet);
            float difference = (System.nanoTime() - packetReceivedTime) * 0.000000001f;
            float latency = connection.getReturnTripTime() * 0.0005f;
            float delta = difference + latency;
            TransformComponent transformComponent = Mappers.TRANSFORM.get(entity);
            MovementComponent movementComponent = Mappers.MOVEMENT.get(entity);
            movementComponent.velocity.y -= movementComponent.getGravity() * delta;
            transformComponent.addPosition(0, movementComponent.velocity.y * delta);
        });
    }

}
