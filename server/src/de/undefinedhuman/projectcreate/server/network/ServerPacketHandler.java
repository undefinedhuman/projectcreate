package de.undefinedhuman.projectcreate.server.network;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.ecs.stats.name.NameComponent;
import de.undefinedhuman.projectcreate.core.inventory.InvItem;
import de.undefinedhuman.projectcreate.core.inventory.Inventory;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.authentication.LoginRequest;
import de.undefinedhuman.projectcreate.core.network.authentication.LoginResponse;
import de.undefinedhuman.projectcreate.core.network.encryption.*;
import de.undefinedhuman.projectcreate.core.network.packets.MousePacket;
import de.undefinedhuman.projectcreate.core.network.packets.SelectorPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.CreateEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.ComponentPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.JumpPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.MovementRequest;
import de.undefinedhuman.projectcreate.core.network.packets.inventory.SelectItemPacket;
import de.undefinedhuman.projectcreate.core.network.packets.inventory.UpdateSlotsPacket;
import de.undefinedhuman.projectcreate.core.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.Utils;
import de.undefinedhuman.projectcreate.engine.utils.ds.Tuple;
import de.undefinedhuman.projectcreate.server.ServerManager;
import de.undefinedhuman.projectcreate.server.utils.IDManager;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ServerPacketHandler implements PacketHandler {

    private HashMap<String, Long> sessionIDs = new HashMap<>();

    @Override
    public void handle(Connection connection, LoginRequest packet) {
        PlayerConnection playerConnection = (PlayerConnection) connection;
        Tuple<String, String> loginData;
        if(playerConnection.decryptionCipher == null || (loginData = LoginRequest.parse(playerConnection.decryptionCipher, packet)) == null)
            return;
        Log.info(loginData);
        Log.info(loginData.getT());
        long currentWorldID = sessionIDs.get(loginData.getT());
        if(currentWorldID >= 0)
            return;
        long worldID = IDManager.getInstance().createNewID();
        Entity player = BlueprintManager.getInstance().createEntity(BlueprintManager.PLAYER_BLUEPRINT_ID, worldID);
        NameComponent nameComponent = Mappers.NAME.get(player);
        if(nameComponent != null)
            nameComponent.setName(loginData.getU());
        // TEMP
        InventoryComponent inventoryComponent = Mappers.INVENTORY.get(player);
        inventoryComponent.getInventory("Inventory").addItem(1, Integer.parseInt(loginData.getU().split(" ")[1]));
        inventoryComponent.getInventory("Inventory").addItem(2, ItemManager.getInstance().getItem(2).maxAmount.getValue());

        inventoryComponent.getInventory("Selector").addItem(1, ItemManager.getInstance().getItem(1).maxAmount.getValue());
        inventoryComponent.getInventory("Selector").addItem(2, ItemManager.getInstance().getItem(2).maxAmount.getValue());
        // TEMP END
        LoginResponse response = LoginResponse.serialize(worldID, PacketUtils.createComponentData(player.getComponents()));
        sessionIDs.put(loginData.getT(), worldID);
        connection.sendTCP(response);
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
    public void handle(Connection connection, MovementRequest packet) {
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
            /*if(packet.direction != 0) {
                transformComponent.setPosition(MovementSystem.moveEntity(transformComponent.getPosition(), new Vector2(packet.direction * movementComponent.getSpeed(), 0), delta));
            } else {
                if(movementComponent.getDirection() != 0)
                    transformComponent.setPosition(MovementSystem.moveEntity(transformComponent.getPosition(), new Vector2(movementComponent.getDirection() * movementComponent.getSpeed(), 0), -1f * delta));
            }
            MovementRequest.parse(entity, packet);*/
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
    public void handle(Connection connection, SelectItemPacket packet) {
        PlayerConnection playerConnection = (PlayerConnection) connection;
        Entity player = EntityManager.getInstance().getEntity(playerConnection.worldID);
        Entity interactedEntity = EntityManager.getInstance().getEntity(packet.interactedEntityID);
        if(player == null || interactedEntity == null) return;
        InventoryComponent playerInventory = Mappers.INVENTORY.get(player);
        InventoryComponent interactedInventoryComponent = Mappers.INVENTORY.get(interactedEntity);
        if(interactedInventoryComponent == null || playerInventory == null || !playerInventory.currentlySelectedItem.isEmpty()) return;
        Inventory interactedInventory = interactedInventoryComponent.getInventory(packet.inventoryName);
        if(interactedInventory == null || !Utils.isInRange(packet.row, 0, interactedInventory.getRow()-1) || !Utils.isInRange(packet.col, 0, interactedInventory.getCol()-1)) return;

        InvItem clickedItem = interactedInventory.getInvItem(packet.row, packet.col);
        if(clickedItem.isEmpty()) return;
        int amount = clickedItem.getAmount() / (packet.half ? 2 : 1);
        clickedItem.removeItem(amount);
        playerInventory.currentlySelectedItem.setItem(clickedItem.getID(), amount);
        ServerManager.getInstance().sendToTCP(connection.getID(),
                UpdateSlotsPacket.serialize(
                        packet.interactedEntityID, packet.inventoryName, packet.row, packet.col, clickedItem.getID(), clickedItem.getAmount(),
                        playerInventory.currentlySelectedItem.getID(), playerInventory.currentlySelectedItem.getAmount()
                )
        );
    }

    private HashMap<Connection, byte[]> verifyTokens = new HashMap<>();

    @Override
    public void handle(Connection connection, InitPacket packet) {
        PlayerConnection playerConnection = (PlayerConnection) connection;
        if(playerConnection.decryptionCipher != null || verifyTokens.containsKey(connection))
            return;
        byte[] verifyToken = ServerEncryption.getInstance().generateVerifyToken();
        verifyTokens.put(connection, verifyToken);
        connection.sendTCP(EncryptionRequest.serialize(ServerEncryption.getInstance().getPublicKey(), verifyToken));
    }

    @Override
    public void handle(Connection connection, EncryptionResponse packet) {
        PlayerConnection playerConnection = (PlayerConnection) connection;
        if(playerConnection.decryptionCipher != null || !verifyTokens.containsKey(connection))
            return;
        Tuple<byte[], byte[]> clientKeys = EncryptionResponse.parse(ServerEncryption.getInstance().getDecryptionCipher(), packet);
        if(clientKeys == null)
            return;
        if(!Arrays.equals(verifyTokens.get(connection), clientKeys.getU()))
            return;
        verifyTokens.remove(connection);
        Key symmetricClientKey = new SecretKeySpec(clientKeys.getT(), "AES");
        try {
            playerConnection.decryptionCipher = Cipher.getInstance("AES");
            playerConnection.decryptionCipher.init(Cipher.DECRYPT_MODE, symmetricClientKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            Log.error("Error while creating temporary client aes cipher", ex);
            playerConnection.decryptionCipher = null;
            return;
        }
        Cipher clientEncryptionCipher;
        try {
            clientEncryptionCipher = Cipher.getInstance("AES");
            clientEncryptionCipher.init(Cipher.ENCRYPT_MODE, symmetricClientKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            Log.error("Error while creating temporary client aes cipher", ex);
            return;
        }
        String sessionID = EncryptionUtils.encodeBase64String(ServerEncryption.getInstance().generateSessionID());
        sessionIDs.put(sessionID, -1L);
        connection.sendTCP(SessionPacket.serialize(clientEncryptionCipher, sessionID));
    }

    @Override
    public void handle(Connection connection, EncryptionPacket packet) {
        Log.info(packet.log());
    }
}
