package de.undefinedhuman.projectcreate.server.network;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.ecs.stats.name.NameComponent;
import de.undefinedhuman.projectcreate.core.inventory.Inventory;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.LoginPacket;
import de.undefinedhuman.projectcreate.core.network.packets.MousePacket;
import de.undefinedhuman.projectcreate.core.network.packets.SelectorPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.CreateEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.ComponentPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.JumpPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.MovementPacket;
import de.undefinedhuman.projectcreate.core.network.packets.inventory.AddItemPacket;
import de.undefinedhuman.projectcreate.core.network.packets.inventory.SelectItemPacket;
import de.undefinedhuman.projectcreate.core.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.engine.utils.Utils;
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
        // TEMP
        InventoryComponent inventoryComponent = Mappers.INVENTORY.get(player);
        inventoryComponent.getInventory("Inventory").addItem(1, Integer.parseInt(packet.name.split(" ")[1]));
        inventoryComponent.getInventory("Inventory").addItem(2, ItemManager.getInstance().getItem(2).maxAmount.getValue());

        inventoryComponent.getInventory("Selector").addItem(1, ItemManager.getInstance().getItem(1).maxAmount.getValue());
        inventoryComponent.getInventory("Selector").addItem(2, ItemManager.getInstance().getItem(2).maxAmount.getValue());
        // TEMP END
        packet.worldID = worldID;
        packet.componentData = PacketUtils.createComponentData(player.getComponents());
        connection.sendTCP(packet);
        ServerManager.getInstance().sendToAllExceptTCP(connection.getID(), CreateEntityPacket.serialize(player));
        EntityManager.getInstance().stream().map(Map.Entry::getValue).forEach(entity -> {
            if(EntityManager.getInstance().isRemoving(Mappers.ID.get(entity).getWorldID()) || entity.isRemoving() || entity.isScheduledForRemoval())
                return;
            connection.sendTCP(CreateEntityPacket.serialize(entity));
        });
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
        long worldID = ((PlayerConnection) connection).worldID;
        Entity entity = EntityManager.getInstance().getEntity(worldID);
        if(entity == null || entity.isScheduledForRemoval() || entity.isRemoving()) return;
        ServerManager.getInstance().sendToAllExceptTCP(connection.getID(), packet);
        long packetReceivedTime = System.nanoTime();
        ServerManager.getInstance().COMMAND_CACHE.add(() -> {
            if(!EntityManager.getInstance().hasEntity(worldID) || !connection.isConnected())
                return;
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
        PlayerConnection playerConnection = (PlayerConnection) connection;
        Entity entity = EntityManager.getInstance().getEntity(playerConnection.worldID);
        if(entity == null) return;
        packet.worldID = playerConnection.worldID;
        ServerManager.getInstance().sendToAllExceptTCP(connection.getID(), packet);
        long packetReceivedTime = System.nanoTime();
        ServerManager.getInstance().COMMAND_CACHE.add(() -> {
            if(!EntityManager.getInstance().hasEntity(playerConnection.worldID) || !connection.isConnected())
                return;
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

    @Override
    public void handle(Connection connection, MousePacket packet) {
        PlayerConnection playerConnection = (PlayerConnection) connection;
        Entity entity = EntityManager.getInstance().getEntity(playerConnection.worldID);
        if(entity == null) return;
        packet.worldID = playerConnection.worldID;
        ServerManager.getInstance().sendToAllExceptUDP(connection.getID(), packet);
        MousePacket.parse(entity, packet);
    }

    @Override
    public void handle(Connection connection, SelectorPacket packet) {
        PlayerConnection playerConnection = (PlayerConnection) connection;
        Entity entity = EntityManager.getInstance().getEntity(playerConnection.worldID);
        if(entity == null) return;
        packet.worldID = playerConnection.worldID;
        ServerManager.getInstance().sendToAllExceptTCP(connection.getID(), packet);
        SelectorPacket.parse(entity, packet);
    }

    @Override
    public void handle(Connection connection, AddItemPacket packet) {
        PlayerConnection playerConnection = (PlayerConnection) connection;
        Entity entity = EntityManager.getInstance().getEntity(playerConnection.worldID);
        if(entity == null) return;
        packet.worldID = playerConnection.worldID;
        Mappers.INVENTORY.get(entity).getInventory("Inventory").addItem(packet.itemID, packet.itemAmount);
        ServerManager.getInstance().sendToTCP(connection.getID(), packet);
    }

    @Override
    public void handle(Connection connection, SelectItemPacket packet) {
        PlayerConnection playerConnection = (PlayerConnection) connection;
        Entity player = EntityManager.getInstance().getEntity(playerConnection.worldID);
        Entity interactedEntity = EntityManager.getInstance().getEntity(packet.interactedEntityID);
        if(player == null || interactedEntity == null) return;
        InventoryComponent playerInventory = Mappers.INVENTORY.get(player);
        InventoryComponent interactedInventoryComponent = Mappers.INVENTORY.get(interactedEntity);
        if(interactedInventoryComponent == null || playerInventory == null) return;
        Inventory interactedInventory = interactedInventoryComponent.getInventory(packet.inventoryName);
        if(interactedInventory == null || !Utils.isInRange(packet.row, 0, interactedInventory.getRow()-1) || !Utils.isInRange(packet.col, 0, interactedInventory.getCol()-1)) return;
        playerInventory.currentlySelectedItem.setItem(interactedInventory.forceRemoveItem(packet.row, packet.col));
        // TEMP
        ServerManager.getInstance().sendToTCP(connection.getID(), packet);
    }
}
