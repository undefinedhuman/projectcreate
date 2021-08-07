package de.undefinedhuman.projectcreate.server.network;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.name.NameComponent;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.LoginPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.CreateEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.RemoveEntityPacket;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.server.ServerManager;
import de.undefinedhuman.projectcreate.server.utils.IDManager;

public class ServerPacketHandler implements PacketHandler {
    @Override
    public void handle(Connection connection, LoginPacket packet) {
        long worldID = IDManager.getInstance().getCurrentMaxWorldID();
        Entity player = BlueprintManager.getInstance().getBlueprint(BlueprintManager.PLAYER_BLUEPRINT_ID).createInstance();
        NameComponent nameComponent = Mappers.NAME.get(player);
        if(nameComponent != null) nameComponent.setName(packet.name);
        ServerManager.getInstance().sendToAllExceptTCP(connection.getID(), CreateEntityPacket.create(BlueprintManager.PLAYER_BLUEPRINT_ID, worldID, player));
        connection.sendTCP(packet);
        EntityManager.getInstance().stream().forEach(entry -> connection.sendTCP(CreateEntityPacket.create(BlueprintManager.PLAYER_BLUEPRINT_ID, entry.getKey(), entry.getValue())));
        EntityManager.getInstance().addEntity(worldID, player);
        ((PlayerConnection) connection).worldID = worldID;
    }

    @Override
    public void handle(Connection connection, CreateEntityPacket packet) {
        EntityManager.getInstance().addEntity(packet.worldID, CreateEntityPacket.parse(packet));
    }

    @Override
    public void handle(Connection connection, RemoveEntityPacket packet) {}

}
