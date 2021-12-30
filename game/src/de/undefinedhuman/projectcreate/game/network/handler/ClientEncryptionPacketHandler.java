package de.undefinedhuman.projectcreate.game.network.handler;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.EntityFlag;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryComponent;
import de.undefinedhuman.projectcreate.core.network.packets.auth.EncryptionPacket;
import de.undefinedhuman.projectcreate.core.network.packets.auth.EncryptionPacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.auth.EncryptionUtils;
import de.undefinedhuman.projectcreate.core.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.game.Main;
import de.undefinedhuman.projectcreate.game.inventory.ClientInventory;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.inventory.player.PlayerInventory;
import de.undefinedhuman.projectcreate.game.inventory.player.Selector;
import de.undefinedhuman.projectcreate.game.network.ClientEncryption;
import de.undefinedhuman.projectcreate.game.network.ClientManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameScreen;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class ClientEncryptionPacketHandler extends EncryptionPacketHandler {

    public ClientEncryptionPacketHandler() {
        registerHandlerFunction(EncryptionPacket.PUBLIC_KEY, this::handlePublicKey);
        registerHandlerFunction(EncryptionPacket.SESSION_ID, this::handleSessionID);
        registerHandlerFunction(EncryptionPacket.CHARACTER_RESPONSE, this::handleCharacterResponse);
    }

    public void handlePublicKey(Connection connection, EncryptionPacket packet) {
        String[] data = packet.getData().split(":");
        if(data.length != 2) {
            Log.error("Received public key data does not follow given specifications!");
            return;
        }
        String verifyToken = data[0];
        byte[] publicKey = EncryptionUtils.decodeBase64String(data[1]);
        ClientEncryption.getInstance().init(publicKey);
        String combinedKeyWithToken = verifyToken + ":" + ClientEncryption.getInstance().getAESKeyAsString();
        connection.sendTCP(EncryptionPacket.serialize(ClientEncryption.getInstance().getRSAEncryptionCipher(), EncryptionPacket.CLIENT_KEY, combinedKeyWithToken));
    }

    public void handleSessionID(Connection connection, EncryptionPacket packet) {
        ClientManager.getInstance().currentSessionID = EncryptionPacket.parse(ClientEncryption.getInstance().getDecryptionAESCipher(), packet);
        Main.getInstance().setScreen(GameScreen.getInstance());
        ClientManager.getInstance().sendTCP(EncryptionPacket.serialize(ClientEncryption.getInstance().getAESEncryptionCipher(), EncryptionPacket.CHARACTER_SELECTION, ClientManager.getInstance().currentSessionID + ":" + "undefinedhuman " + Tools.RANDOM.nextInt(100)));
    }

    public void handleCharacterResponse(Connection connection, EncryptionPacket packet) {
        String[] data = EncryptionPacket.parse(null, packet).split(":", 2);
        Long worldID;
        if(data.length != 2 || (worldID = Tools.isLong(data[0])) == null)
            return;
        Entity player = BlueprintManager.getInstance().createEntity(BlueprintManager.PLAYER_BLUEPRINT_ID, worldID, EntityFlag.getBigMask(EntityFlag.IS_MAIN_PLAYER));
        PacketUtils.setComponentData(player, PacketUtils.parseComponentData(data[1]));
        Mappers.MOVEMENT.get(player).predictedPosition.set(Mappers.TRANSFORM.get(player).getPosition());
        EntityManager.getInstance().addEntity(worldID, player);
        GameManager.getInstance().player = player;
        linkInventories(player, PlayerInventory.getInstance(), Selector.getInstance());
        InventoryComponent component = Mappers.INVENTORY.get(player);
        InventoryManager.getInstance().getDragAndDrop().getDraggableItem().link(component.currentlySelectedItem);
        Main.getInstance().setScreen(GameScreen.getInstance());
    }

    private void linkInventories(Entity entity, ClientInventory<?>... inventories) {
        for(ClientInventory<?> inventory : inventories)
            inventory.linkInventory(entity);
    }


}
