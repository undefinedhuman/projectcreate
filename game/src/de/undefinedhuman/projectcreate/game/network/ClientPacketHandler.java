package de.undefinedhuman.projectcreate.game.network;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.EntityFlag;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.authentication.LoginRequest;
import de.undefinedhuman.projectcreate.core.network.authentication.LoginResponse;
import de.undefinedhuman.projectcreate.core.network.encryption.EncryptionRequest;
import de.undefinedhuman.projectcreate.core.network.encryption.EncryptionResponse;
import de.undefinedhuman.projectcreate.core.network.encryption.SessionPacket;
import de.undefinedhuman.projectcreate.core.network.packets.MousePacket;
import de.undefinedhuman.projectcreate.core.network.packets.SelectorPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.ComponentPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.PositionPacket;
import de.undefinedhuman.projectcreate.core.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.engine.utils.ds.Tuple;
import de.undefinedhuman.projectcreate.game.Main;
import de.undefinedhuman.projectcreate.game.inventory.ClientInventory;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.inventory.player.PlayerInventory;
import de.undefinedhuman.projectcreate.game.inventory.player.Selector;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameScreen;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class ClientPacketHandler implements PacketHandler {
    @Override
    public void handle(Connection connection, LoginResponse packet) {
        Entity player = BlueprintManager.getInstance().createEntity(BlueprintManager.PLAYER_BLUEPRINT_ID, packet.worldID, EntityFlag.getBigMask(EntityFlag.IS_MAIN_PLAYER));
        PacketUtils.setComponentData(player, PacketUtils.parseComponentData(packet.componentData));
        Mappers.MOVEMENT.get(player).predictedPosition.set(Mappers.TRANSFORM.get(player).getPosition());
        EntityManager.getInstance().addEntity(packet.worldID, player);
        GameManager.getInstance().player = player;
        linkInventories(player, PlayerInventory.getInstance(), Selector.getInstance());
        InventoryComponent component = Mappers.INVENTORY.get(player);
        InventoryManager.getInstance().getDragAndDrop().getDraggableItem().link(component.currentlySelectedItem);
        Main.instance.setScreen(GameScreen.getInstance());
    }

    private void linkInventories(Entity entity, ClientInventory<?>... inventories) {
        for(ClientInventory<?> inventory : inventories)
            inventory.linkInventory(entity);
    }

    @Override
    public void handle(Connection connection, CreateEntityPacket packet) {
        Entity entity = CreateEntityPacket.parse(packet);
        if(entity == null || entity.isScheduledForRemoval() || entity.isRemoving()) return;
        EntityManager.getInstance().addEntity(packet.worldID, entity);
    }

    @Override
    public void handle(Connection connection, RemoveEntityPacket packet) {
        EntityManager.getInstance().removeEntity(packet.worldID);
    }

    @Override
    public void handle(Connection connection, ComponentPacket packet) {
        Entity entity = EntityManager.getInstance().getEntity(packet.worldID);
        if(entity == null || entity.isScheduledForRemoval() || entity.isRemoving()) return;
        ComponentPacket.parse(entity, packet);
    }

    @Override
    public void handle(Connection connection, MovementResponse packet) {
        Entity entity = EntityManager.getInstance().getEntity(packet.worldID);
        if(entity == null) return;
        MovementResponse.parse(packet);
    }

    private Vector2 TEMP_POSITION = new Vector2();

    @Override
    public void handle(Connection connection, PositionPacket packet) {
        Entity entity = EntityManager.getInstance().getEntity(packet.worldID);
        if(entity == null || entity.isScheduledForRemoval() || entity.isRemoving()) return;
        MovementComponent movementComponent = Mappers.MOVEMENT.get(entity);

        if(!(packet.timeStamp > movementComponent.latestPositionPacketTime))
            return;
        movementComponent.latestPositionPacketTime = packet.timeStamp;

        if(movementComponent.lastPositionPacketTimeLocal == 0)
            movementComponent.lastPositionPacketTimeLocal = System.nanoTime();

        if(entity != GameManager.getInstance().player) movementComponent.movementHistory.add(new MovementComponent.MovementFrame(packet, (System.nanoTime() - movementComponent.lastPositionPacketTimeLocal) * 0.000000001f));
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

            movementComponent.predictedPosition = new Vector2(packet.x, packet.y);
            for(MovementComponent.MovementFrame frame : movementComponent.movementHistory) {
                TEMP_POSITION.set(movementComponent.predictedPosition).mulAdd(frame.velocity, frame.delta);
                frame.position.set(TEMP_POSITION).sub(movementComponent.predictedPosition);
                movementComponent.predictedPosition.set(TEMP_POSITION);
            }

            if(movementComponent.predictedPosition.y <= 0) movementComponent.predictedPosition.y = 0;
        }
        movementComponent.lastPositionPacketTimeLocal = System.nanoTime();
    }

    @Override
    public void handle(Connection connection, JumpPacket packet) {
        Entity entity = EntityManager.getInstance().getEntity(packet.worldID);
        if(entity == null) return;
        MovementComponent component = Mappers.MOVEMENT.get(entity);
        if(component == null) return;
        component.forceJump();
    }

    @Override
    public void handle(Connection connection, MousePacket packet) {
        Entity entity = EntityManager.getInstance().getEntity(packet.worldID);
        if(entity == null) return;
        MousePacket.parse(entity, packet);
    }

    @Override
    public void handle(Connection connection, SelectorPacket packet) {
        Entity entity = EntityManager.getInstance().getEntity(packet.worldID);
        if(entity == null) return;
        SelectorPacket.parse(entity, packet);
    }

    @Override
    public void handle(Connection connection, UpdateSlotsPacket packet) {
        Entity entity1 = EntityManager.getInstance().getEntity(packet.entityID1);
        if(entity1 == null) return;
        InventoryComponent component1 = Mappers.INVENTORY.get(entity1);
        component1.getInventory(packet.inventoryName1).getInvItem(packet.row1, packet.col1).setItem(packet.itemID1, packet.amount1);
        InventoryComponent component2 = Mappers.INVENTORY.get(GameManager.getInstance().player);
        component2.currentlySelectedItem.setItem(packet.itemID2, packet.amount2);
    }

    @Override
    public void handle(Connection connection, EncryptionRequest packet) {
        Tuple<byte[], byte[]> decryptedServerData = EncryptionRequest.parse(packet);
        ClientEncryption.getInstance().init(decryptedServerData.getT());
        ClientManager.getInstance().sendTCP(
                EncryptionResponse.serialize(
                        ClientEncryption.getInstance().getRSAEncryptionCipher(),
                        ClientEncryption.getInstance().getAesKey().getEncoded(),
                        decryptedServerData.getU()
                )
        );
    }

    @Override
    public void handle(Connection connection, SessionPacket packet) {
        ClientManager.getInstance().currentSessionID = SessionPacket.parse(ClientEncryption.getInstance().getDecryptionAESCipher(), packet);
        ClientManager.getInstance().sendTCP(LoginRequest.serialize(ClientEncryption.getInstance().getAESEncryptionCipher(), "undefinedhuman " + Tools.RANDOM.nextInt(100), ClientManager.getInstance().currentSessionID));
    }
}
