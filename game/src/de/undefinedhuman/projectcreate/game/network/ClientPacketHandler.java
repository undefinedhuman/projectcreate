package de.undefinedhuman.projectcreate.game.network;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.LoginPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.CreateEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.RemoveEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.ComponentPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.MovementPacket;
import de.undefinedhuman.projectcreate.core.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.game.Main;
import de.undefinedhuman.projectcreate.game.entity.ecs.system.MovementSystem;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameScreen;

public class ClientPacketHandler implements PacketHandler {
    @Override
    public void handle(Connection connection, LoginPacket packet) {
        Entity player = BlueprintManager.getInstance().createEntity(BlueprintManager.PLAYER_BLUEPRINT_ID, packet.worldID);
        PacketUtils.setComponentData(player, PacketUtils.parseComponentData(packet.componentData));
        EntityManager.getInstance().addEntity(packet.worldID, player);
        GameManager.getInstance().player = player;
        Main.instance.setScreen(GameScreen.getInstance());
    }

    @Override
    public void handle(Connection connection, CreateEntityPacket packet) {
        Entity entity = CreateEntityPacket.parse(packet);
        if(entity == null) return;
        EntityManager.getInstance().addEntity(packet.worldID, entity);
    }

    @Override
    public void handle(Connection connection, RemoveEntityPacket packet) {
        EntityManager.getInstance().removeEntity(packet.worldID);
    }

    @Override
    public void handle(Connection connection, ComponentPacket packet) {
        Entity entity = EntityManager.getInstance().getEntity(packet.worldID);
        if(entity == null) return;
        ComponentPacket.parse(entity, packet);
    }

    @Override
    public void handle(Connection connection, MovementPacket packet) {
        Entity entity = EntityManager.getInstance().getEntity(packet.worldID);
        if(entity == null) return;
        MovementPacket.parse(entity, packet);
        MovementSystem system = EntityManager.getInstance().getSystem(MovementSystem.class);
        system.processEntity(entity, ClientManager.getInstance().getReturnTime() / 1000f);
    }

}
