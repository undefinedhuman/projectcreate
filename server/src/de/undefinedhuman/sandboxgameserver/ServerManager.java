package de.undefinedhuman.sandboxgameserver;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.EntityManager;
import de.undefinedhuman.sandboxgameserver.entity.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.sandboxgameserver.log.Log;
import de.undefinedhuman.sandboxgameserver.network.ServerListener;
import de.undefinedhuman.sandboxgameserver.network.packets.PacketManager;
import de.undefinedhuman.sandboxgameserver.network.packets.ServerClosedPacket;
import de.undefinedhuman.sandboxgameserver.network.packets.ServerClosedReason;
import de.undefinedhuman.sandboxgameserver.utils.ResourceManager;
import de.undefinedhuman.sandboxgameserver.utils.threading.ThreadManager;
import de.undefinedhuman.sandboxgameserver.world.WorldGenerator;
import de.undefinedhuman.sandboxgameserver.world.WorldManager;
import de.undefinedhuman.sandboxgameserver.world.settings.BiomeSetting;
import de.undefinedhuman.sandboxgameserver.world.settings.WorldPreset;
import de.undefinedhuman.sandboxgameserver.world.settings.WorldSetting;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ServerManager {

    public static ServerManager instance;

    private Server server;
    private Timer logUpdateThread, playerUpdate;

    public ServerManager() {

        initManager();

        server = new Server(1048576,1048576);

        PacketManager.register(server);
        server.addListener(new ServerListener());
        try { server.bind(56098, 56099); } catch (IOException e) { Log.instance.info(e.getMessage()); }
        server.start();

        logUpdateThread = new Timer();
        logUpdateThread.schedule(new TimerTask() {
            @Override
            public void run() { Log.instance.save(); }
        }, 0, 900000);

        playerUpdate = new Timer();
        playerUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                for(Entity entity : WorldManager.instance.getPlayers()) if(!entity.c.isConnected()) WorldManager.instance.getWorld(entity.getWorldName()).removeEntity(entity.getWorldID());
            }
        },0,30000);

        BlueprintManager.instance.loadBlueprint(0);

        if (WorldManager.instance.worldExists("Main")) WorldManager.instance.loadWorld("Main");
        else WorldManager.instance.generateWorld(new WorldPreset("Main", WorldSetting.DEV, BiomeSetting.DEV));

    }

    public Server getServer() {
        return server;
    }

    private void initManager() {

        ResourceManager.instance = new ResourceManager();
        Log.instance = new Log();
        Log.instance.load();
        ThreadManager.instance = new ThreadManager();
        BlueprintManager.instance = new BlueprintManager();
        EntityManager.instance = new EntityManager();
        WorldGenerator.instance = new WorldGenerator();
        WorldManager.instance = new WorldManager();

    }

    public void sendToTCP(Connection c, Object object) {
        server.sendToTCP(c.getID(), object);
    }

    public void sendToTCP(int id, Object object) {
        server.sendToTCP(id, object);
    }

    public void sendToUDP(int id, Object object) {
        server.sendToUDP(id, object);
    }

    public void sendAllTcp(Object object) {
        server.sendToAllTCP(object);
    }

    public void sendToAllUDP(Object object) {
        server.sendToAllUDP(object);
    }

    public void sendToAllFromWorldExceptID(int id, String worldName, Object object) {
        synchronized(WorldManager.instance.getWorld(worldName).getPlayers()) {
            if (WorldManager.instance.hasWorld(worldName))
                for (Entity entity : WorldManager.instance.getWorld(worldName).getPlayers())
                    if (entity.c.isConnected()) {
                        int cID = entity.c.getID();
                        if (cID != id) server.sendToTCP(cID, object);
                    }
        }
    }

    public void sendToAllExceptTCP(int id, Object object) {
        server.sendToAllExceptTCP(id, object);
    }

    public void sendToAllExceptUDP(int id, Object object) {
        server.sendToAllExceptUDP(id, object);
    }

    public void closeServer() {

        ServerClosedPacket packet = new ServerClosedPacket();
        packet.reason = ServerClosedReason.SERVER_CLOSED;
        sendAllTcp(packet);

        ThreadManager.instance.delete();
        WorldManager.instance.delete();
        logUpdateThread.cancel();
        playerUpdate.cancel();
        Log.instance.save();
        server.stop();

    }

}
