package de.undefinedhuman.sandboxgameserver.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.undefinedhuman.sandboxgameserver.ServerManager;
import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.EntityManager;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.entity.ecs.components.equip.EquipComponent;
import de.undefinedhuman.sandboxgameserver.entity.ecs.components.movement.MovementComponent;
import de.undefinedhuman.sandboxgameserver.entity.ecs.components.name.NameComponent;
import de.undefinedhuman.sandboxgameserver.file.FileReader;
import de.undefinedhuman.sandboxgameserver.file.FsFile;
import de.undefinedhuman.sandboxgameserver.file.Paths;
import de.undefinedhuman.sandboxgameserver.log.Log;
import de.undefinedhuman.sandboxgameserver.network.packets.LoginPacket;
import de.undefinedhuman.sandboxgameserver.network.packets.entity.AddEntityPacket;
import de.undefinedhuman.sandboxgameserver.network.packets.entity.ComponentPacket;
import de.undefinedhuman.sandboxgameserver.network.packets.entity.RemoveEntityPacket;
import de.undefinedhuman.sandboxgameserver.network.packets.inventory.EquipPacket;
import de.undefinedhuman.sandboxgameserver.network.packets.player.AddPlayerPacket;
import de.undefinedhuman.sandboxgameserver.network.packets.player.JumpPacket;
import de.undefinedhuman.sandboxgameserver.network.packets.world.BlockPacket;
import de.undefinedhuman.sandboxgameserver.network.packets.world.WorldLayerPacket;
import de.undefinedhuman.sandboxgameserver.network.packets.world.WorldPacket;
import de.undefinedhuman.sandboxgameserver.network.utils.PacketUtils;
import de.undefinedhuman.sandboxgameserver.utils.threading.TaskType;
import de.undefinedhuman.sandboxgameserver.utils.threading.ThreadManager;
import de.undefinedhuman.sandboxgameserver.utils.threading.ThreadTask;
import de.undefinedhuman.sandboxgameserver.world.World;
import de.undefinedhuman.sandboxgameserver.world.WorldManager;

import java.util.HashMap;

public class ServerListener extends Listener {

    private HashMap<Integer, Entity> players;

    public ServerListener() {
        players = new HashMap<>();
    }

    @Override
    public void connected(Connection connection) {}

    @Override
    public void disconnected(Connection connection) {

        int id = connection.getID();
        if (players.containsKey(id)) {

            Entity entity = players.get(id);

            for (Entity worldPlayer : WorldManager.instance.getWorld(entity.getWorldName()).getPlayers()) {

                RemoveEntityPacket packet = new RemoveEntityPacket();
                packet.worldID = entity.getWorldID();
                ServerManager.instance.sendToTCP(worldPlayer.c, packet);

            }

            Log.instance.info(((NameComponent) entity.getComponent(ComponentType.NAME)).getName() + " has disconnected!");

            EntityManager.instance.savePlayer(entity.getWorldName(), entity);
            WorldManager.instance.getWorld(entity.getWorldName()).removeEntity(entity.getWorldID());
            players.remove(id);

        }

    }

    @Override
    public void received(Connection connection, Object o) {

        if (o instanceof LoginPacket) {

            LoginPacket packet = (LoginPacket) o;
            boolean loggedIn = WorldManager.instance.isPlayerOnline(packet.name) || players.containsKey(connection.getID());

            packet.loggedIn = !loggedIn;
            ServerManager.instance.sendToTCP(connection.getID(), packet);
            if (loggedIn) return;

            Log.instance.info(packet.name + " is connected!");

            String playerName = packet.name, worldName;
            Entity player;

            if (EntityManager.instance.playerExists(playerName)) {
                FsFile file = new FsFile(Paths.PLAYER_PATH, playerName + ".player", false, false);
                FileReader reader = file.getFileReader(true);
                reader.nextLine();
                worldName = reader.getNextString();
                player = EntityManager.instance.loadPlayer(reader);
                reader.close();
            } else {
                player = EntityManager.instance.generatePlayer(playerName);
                worldName = "Main";
            }

            player.c = connection;
            players.put(connection.getID(), player);

            ThreadManager.instance.addTask(TaskType.WORLDLOAD, new ThreadTask() {
                @Override
                public void runTask() {

                    if (!WorldManager.instance.hasWorld(worldName)) {

                        WorldManager.instance.load(worldName);
                        Log.instance.info("World " + worldName + " loaded!");

                    }

                }

                @Override
                public void endTask() {

                    int worldID = WorldManager.instance.getNewID();
                    String playerInfo = player.getEntityInfo();

                    AddPlayerPacket addPlayerPacket = new AddPlayerPacket();
                    addPlayerPacket.worldID = worldID;
                    addPlayerPacket.playerInfo = playerInfo;
                    ServerManager.instance.sendToTCP(connection.getID(), addPlayerPacket);

                    AddEntityPacket addEntityPacket = new AddEntityPacket();
                    addEntityPacket.worldID = worldID;
                    addEntityPacket.blueprintID = 0;
                    addEntityPacket.entityInfo = playerInfo;

                    World world = WorldManager.instance.getWorld(worldName);

                    for (Entity entity : world.getPlayers()) {

                        entity.c.sendTCP(addEntityPacket);
                        AddEntityPacket addOtherEntityPacket = new AddEntityPacket();
                        addOtherEntityPacket.worldID = entity.getWorldID();
                        addOtherEntityPacket.blueprintID = 0;
                        addOtherEntityPacket.entityInfo = entity.getEntityInfo();
                        connection.sendTCP(addOtherEntityPacket);

                    }

                    WorldManager.instance.getWorld(worldName).addEntity(worldID, player);

                }

            });

        }

        if (players.containsKey(connection.getID())) {

            Entity entity = players.get(connection.getID());

            if (o instanceof WorldPacket) {

                WorldPacket packet = (WorldPacket) o;
                World world = WorldManager.instance.getWorld(entity.getWorldName());
                packet.worldName = entity.getWorldName();
                packet.width = world.width;
                packet.height = world.height;
                connection.sendTCP(packet);

            }

            if (o instanceof WorldLayerPacket) {

                ThreadManager.instance.addTask(TaskType.WORLDSEND, new ThreadTask() {

                    @Override
                    public void runTask() {

                        WorldLayerPacket packet = (WorldLayerPacket) o;
                        World world = WorldManager.instance.getWorld(packet.name);
                        packet.layer = packet.main ? world.mainLayer.getLayer() : world.backLayer.getLayer();
                        connection.sendTCP(packet);

                    }

                    @Override
                    public void endTask() {}

                });

            }

            if (o instanceof JumpPacket) {

                JumpPacket packet = (JumpPacket) o;
                ((MovementComponent) entity.getComponent(ComponentType.MOVEMENT)).forceJump();
                ServerManager.instance.sendToAllExceptTCP(connection.getID(), packet);

            }

            if (o instanceof ComponentPacket) {

                ComponentPacket packet = (ComponentPacket) o;
                PacketUtils.handleComponentPacket(packet);
                ServerManager.instance.sendToAllFromWorldExceptID(connection.getID(), packet.worldName, packet);

            }

            if (o instanceof BlockPacket) {

                BlockPacket packet = (BlockPacket) o;
                PacketUtils.handleBlockPacket(packet);
                ServerManager.instance.sendToAllFromWorldExceptID(connection.getID(), packet.worldName, packet);

            }

            if (o instanceof EquipPacket) {

                EquipPacket packet = (EquipPacket) o;
                Entity player = players.get(connection.getID());
                packet.entityID = player.getWorldID();
                EquipComponent equipComponent = (EquipComponent) player.getComponent(ComponentType.EQUIP);
                equipComponent.itemIDs[packet.equipID] = packet.equipedItemID;
                ServerManager.instance.sendToAllFromWorldExceptID(connection.getID(), player.getWorldName(), packet);

            }

        }

    }

}
