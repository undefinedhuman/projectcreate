package de.undefinedhuman.projectcreate.core.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.engine.entity.components.movement.MovementComponent;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.core.entity.Entity;
import de.undefinedhuman.projectcreate.core.entity.EntityManager;
import de.undefinedhuman.projectcreate.core.entity.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.core.network.packets.LoginPacket;
import de.undefinedhuman.projectcreate.core.network.packets.ServerClosedPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.AddEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.ComponentPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.RemoveEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.inventory.EquipPacket;
import de.undefinedhuman.projectcreate.core.network.packets.player.AddPlayerPacket;
import de.undefinedhuman.projectcreate.core.network.packets.player.JumpPacket;
import de.undefinedhuman.projectcreate.core.network.packets.world.BlockPacket;
import de.undefinedhuman.projectcreate.core.network.packets.world.WorldLayerPacket;
import de.undefinedhuman.projectcreate.core.network.packets.world.WorldPacket;
import de.undefinedhuman.projectcreate.core.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.core.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.core.world.World;

public class ClientListener extends Listener {

    @Override
    public void connected(Connection connection) {}

    @Override
    public void disconnected(Connection connection) {
        ClientManager.instance.connected = false;
    }

    public void received(Connection c, Object o) {

        if (o instanceof LoginPacket) {

            LoginPacket packet = (LoginPacket) o;
            if (packet.loggedIn) Log.info(packet.name + " connected!");
            else {
//                Main.instance.setScreen(MenuScreen.instance);
//                MenuScreen.instance.setErrorMessage("You are already logged in!");
//                Log.info(packet.name + " is already connected!");
            }

        }

        if (o instanceof AddPlayerPacket) {

            AddPlayerPacket packet = (AddPlayerPacket) o;
            Entity player = BlueprintManager.getInstance().getBlueprint(0).createInstance();
            player.mainPlayer = true;
            // player.receive(packet.playerInfo);
            player.setWorldID(packet.worldID);
            GameManager.instance.player = player;
            EntityManager.getInstance().addEntity(packet.worldID, player);

            ClientManager.instance.connected = true;

//            Main.instance.setScreen(LoadingScreen.instance);

        }

        if (ClientManager.instance.connected) {

            if (o instanceof AddEntityPacket) {

                AddEntityPacket packet = (AddEntityPacket) o;
                Entity entity = BlueprintManager.getInstance().getBlueprint(packet.blueprintID).createInstance();
                // entity.receive(packet.entityInfo);
                entity.setWorldID(packet.worldID);
                EntityManager.getInstance().addEntity(packet.worldID, entity);

            }

            if (o instanceof RemoveEntityPacket) {
                RemoveEntityPacket packet = (RemoveEntityPacket) o;
                EntityManager.getInstance().removeEntity(packet.worldID);
            }

            if (o instanceof ServerClosedPacket) {
//                ClientManager.instance.connected = false;
//                Main.instance.setScreen(MenuScreen.instance);
//                MenuScreen.instance.setErrorMessage("Server closed!");
            }

            if (o instanceof WorldPacket) {

                // TODO Add maxHeight

                WorldPacket packet = (WorldPacket) o;
                World.instance = new World(packet.worldName, 50, packet.width, packet.height, 0);
                EntityManager.getInstance().init();
//                LoadingScreen.instance.worldLoaded = true;

            }

            if (o instanceof WorldLayerPacket) {
//
//                WorldLayerPacket packet = (WorldLayerPacket) o;
//                LoadingScreen.instance.loadLayer(packet);

            }

            if (o instanceof JumpPacket) {

                JumpPacket packet = (JumpPacket) o;
                ((MovementComponent) EntityManager.getInstance().getEntity(packet.id).getComponent(ComponentType.MOVEMENT)).setJump();

            }

            if (o instanceof ComponentPacket) {

                ComponentPacket packet = (ComponentPacket) o;
                PacketUtils.handleComponentPacket(packet);

            }

            if (o instanceof BlockPacket) {

                BlockPacket packet = (BlockPacket) o;
                PacketUtils.handleBlockPacket(packet);

            }

            if (o instanceof EquipPacket) {

                EquipPacket packet = (EquipPacket) o;
                PacketUtils.handleEquipComponent(packet);

            }

        }

    }

}
