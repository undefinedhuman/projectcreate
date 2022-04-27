package de.undefinedhuman.projectcreate.server.network.handler;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryComponent;
import de.undefinedhuman.projectcreate.core.ecs.stats.name.NameComponent;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.core.network.packets.auth.EncryptionPacket;
import de.undefinedhuman.projectcreate.core.network.packets.auth.EncryptionPacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.auth.EncryptionUtils;
import de.undefinedhuman.projectcreate.core.network.packets.entity.CreateEntityPacket;
import de.undefinedhuman.projectcreate.core.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.engine.ecs.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.EntityManager;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.server.ServerManager;
import de.undefinedhuman.projectcreate.server.network.PlayerConnection;
import de.undefinedhuman.projectcreate.server.network.ServerEncryption;
import de.undefinedhuman.projectcreate.server.network.session.SessionManager;
import de.undefinedhuman.projectcreate.server.utils.IDManager;

import java.util.Arrays;
import java.util.HashMap;

public class ServerEncryptionPacketHandler extends EncryptionPacketHandler {

    private HashMap<Connection, byte[]> verifyTokens = new HashMap<>();

    public ServerEncryptionPacketHandler() {
        registerHandlerFunction(EncryptionPacket.INIT, (connection, packet) -> handleInit(connection));
        registerHandlerFunction(EncryptionPacket.CLIENT_KEY, this::handleClientKey);
        registerHandlerFunction(EncryptionPacket.CHARACTER_SELECTION, this::handleCharacterSelection);
    }

    private void handleInit(Connection connection) {
        if(verifyTokens.containsKey(connection))
            return;
        byte[] verifyToken = ServerEncryption.getInstance().generateVerifyToken();
        String encodedVerifyToken = EncryptionUtils.encodeBase64String(verifyToken);
        String encodedPublicKey = EncryptionUtils.encodeBase64String(ServerEncryption.getInstance().getPublicKey().getEncoded());
        verifyTokens.put(connection, verifyToken);
        connection.sendTCP(EncryptionPacket.serialize(null, EncryptionPacket.PUBLIC_KEY, encodedVerifyToken + ":" + encodedPublicKey));
    }

    private void handleClientKey(Connection connection, EncryptionPacket packet) {
        if(!verifyTokens.containsKey(connection))
            return;
        String[] data = EncryptionPacket.parse(ServerEncryption.getInstance().getDecryptionCipher(), packet).split(":");
        if(data.length != 2) {
            Log.error("Received client key data does not follow given specifications!");
            return;
        }
        byte[] verifyToken = EncryptionUtils.decodeBase64String(data[0]);
        byte[] clientKey = EncryptionUtils.decodeBase64String(data[1]);
        if(!Arrays.equals(verifyTokens.get(connection), verifyToken)) {
            Log.error("Received verify token does not match the one generated earlier!");
            return;
        } else verifyTokens.remove(connection);
        if(!(connection instanceof PlayerConnection playerConnection)) return;
        playerConnection.init(clientKey);

        String sessionID = EncryptionUtils.encodeBase64String(ServerEncryption.getInstance().generateSessionID());
        SessionManager.getInstance().registerSession(sessionID);
        connection.sendTCP(EncryptionPacket.serialize(playerConnection.getEncryptionCipher(), EncryptionPacket.SESSION_ID, sessionID));
    }

    private void handleCharacterSelection(Connection connection, EncryptionPacket packet) {
        if(!(connection instanceof PlayerConnection playerConnection))
            return;
        if(playerConnection.getDecryptionCipher() == null)
            return;
        String[] data = EncryptionPacket.parse(playerConnection.getDecryptionCipher(), packet).split(":");
        if(data.length != 2) {
            Log.error("Received client key data does not follow given specifications!");
            return;
        }
        String sessionID = data[0];
        String name = data[1];
        if(SessionManager.getInstance().getWorldID(sessionID) != -1L) {
            Log.error("User is already playing!");
            return;
        }
        long worldID = IDManager.getInstance().createNewID();
        Entity player = EntityManager.getInstance().createEntity(BlueprintManager.PLAYER_BLUEPRINT_ID, worldID);
        NameComponent nameComponent = Mappers.NAME.get(player);
        if(nameComponent != null)
            nameComponent.setName(name);
        // TEMP
        ItemManager.getInstance().loadItems(0, 1, 2);
        InventoryComponent inventoryComponent = Mappers.INVENTORY.get(player);
        inventoryComponent.getInventory("Inventory").addItem(1, Integer.parseInt(name.split(" ")[1]));
        inventoryComponent.getInventory("Inventory").addItem(2, ItemManager.getInstance().getItem(2).maxAmount.getValue());

        inventoryComponent.getInventory("Selector").addItem(1, ItemManager.getInstance().getItem(1).maxAmount.getValue());
        inventoryComponent.getInventory("Selector").addItem(2, ItemManager.getInstance().getItem(2).maxAmount.getValue());
        // TEMP END
        SessionManager.getInstance().setWorldID(sessionID, worldID);
        EntityManager.getInstance().addEntity(player);
        ((PlayerConnection) connection).worldID = worldID;
        connection.sendTCP(EncryptionPacket.serialize(null, EncryptionPacket.CHARACTER_RESPONSE, worldID + ":" + PacketUtils.createComponentData(player.getComponents())));
        ServerManager.getInstance().sendToAllExceptTCP(connection, CreateEntityPacket.serialize(player));
        EntityManager.getInstance().getEntities().forEach(entity -> {
            if(entity.getWorldID() == worldID || entity.isScheduledForRemoval())
                return;
            connection.sendTCP(CreateEntityPacket.serialize(entity));
        });
    }

}
