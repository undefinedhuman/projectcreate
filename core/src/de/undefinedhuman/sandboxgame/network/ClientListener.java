package de.undefinedhuman.sandboxgame.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.EntityManager;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.entity.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.sandboxgame.entity.ecs.components.movement.MovementComponent;
import de.undefinedhuman.sandboxgame.network.packets.LoginPacket;
import de.undefinedhuman.sandboxgame.network.packets.ServerClosedPacket;
import de.undefinedhuman.sandboxgame.network.packets.entity.AddEntityPacket;
import de.undefinedhuman.sandboxgame.network.packets.entity.ComponentPacket;
import de.undefinedhuman.sandboxgame.network.packets.entity.RemoveEntityPacket;
import de.undefinedhuman.sandboxgame.network.packets.inventory.EquipPacket;
import de.undefinedhuman.sandboxgame.network.packets.player.AddPlayerPacket;
import de.undefinedhuman.sandboxgame.network.packets.player.JumpPacket;
import de.undefinedhuman.sandboxgame.network.packets.world.BlockPacket;
import de.undefinedhuman.sandboxgame.network.packets.world.WorldLayerPacket;
import de.undefinedhuman.sandboxgame.network.packets.world.WorldPacket;
import de.undefinedhuman.sandboxgame.network.utils.PacketUtils;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameManager;
import de.undefinedhuman.sandboxgame.world.World;

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
            Entity player = BlueprintManager.instance.getBlueprint(0).createInstance();
            player.mainPlayer = true;
            player.receive(packet.playerInfo);
            player.setWorldID(packet.worldID);
            GameManager.instance.player = player;
            EntityManager.instance.addEntity(packet.worldID, player);

            ClientManager.instance.connected = true;

//            Main.instance.setScreen(LoadingScreen.instance);

        }

        if (ClientManager.instance.connected) {

            if (o instanceof AddEntityPacket) {

                AddEntityPacket packet = (AddEntityPacket) o;
                Entity entity = BlueprintManager.instance.getBlueprint(packet.blueprintID).createInstance();
                entity.receive(packet.entityInfo);
                entity.setWorldID(packet.worldID);
                EntityManager.instance.addEntity(packet.worldID, entity);

            }

            if (o instanceof RemoveEntityPacket) {
                RemoveEntityPacket packet = (RemoveEntityPacket) o;
                EntityManager.instance.removeEntity(packet.worldID);
            }

            if (o instanceof ServerClosedPacket) {
//                ClientManager.instance.connected = false;
//                Main.instance.setScreen(MenuScreen.instance);
//                MenuScreen.instance.setErrorMessage("Server closed!");
            }

            if (o instanceof WorldPacket) {

                WorldPacket packet = (WorldPacket) o;
                World.instance = new World(packet.worldName, packet.width, packet.height, 0);
                EntityManager.instance.init();
//                LoadingScreen.instance.worldLoaded = true;

            }

            if (o instanceof WorldLayerPacket) {
//
//                WorldLayerPacket packet = (WorldLayerPacket) o;
//                LoadingScreen.instance.loadLayer(packet);

            }

            if (o instanceof JumpPacket) {

                JumpPacket packet = (JumpPacket) o;
                ((MovementComponent) EntityManager.instance.getEntity(packet.id).getComponent(ComponentType.MOVEMENT)).forceJump();

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
