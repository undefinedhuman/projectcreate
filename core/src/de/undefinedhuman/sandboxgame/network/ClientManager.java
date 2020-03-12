package de.undefinedhuman.sandboxgame.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.utils.Manager;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.entity.ecs.components.mouse.AngleComponent;
import de.undefinedhuman.sandboxgame.inventory.InvItem;
import de.undefinedhuman.sandboxgame.inventory.InventoryManager;
import de.undefinedhuman.sandboxgame.network.packets.PacketManager;
import de.undefinedhuman.sandboxgame.network.packets.entity.ComponentPacket;
import de.undefinedhuman.sandboxgame.network.utils.PacketUtils;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameManager;
import de.undefinedhuman.sandboxgame.utils.Timer;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.io.IOException;

public class ClientManager extends Manager {

    public static ClientManager instance;

    public String ip = "127.0.0.1";
    public boolean connected = false;
    private Timer playerUpdateTimer60, playerUpdateTimer10;
    private Client client;

    public ClientManager() {

        client = new Client(1048576, 1048576);
        client.start();

        PacketManager.register(client);

        client.addListener(new Listener.QueuedListener(new ClientListener()) {
            protected void queue(Runnable runnable) {
                Gdx.app.postRunnable(runnable);
            }
        });

        playerUpdateTimer60 = new Timer(0.015f, true) {
            @Override
            public void action() {
                Entity player = GameManager.instance.player;
                ((AngleComponent) player.getComponent(ComponentType.ANGLE)).mousePos = Tools.getWorldCoordsOfMouse(GameManager.gameCamera);
                ComponentPacket packet = PacketUtils.createComponentPacket(player, ComponentType.ANGLE, ComponentType.TRANSFORM);
                ClientManager.instance.sendUDP(packet);
            }
        };

        playerUpdateTimer10 = new Timer(0.1f, true) {
            @Override
            public void action() {

                Entity player = GameManager.instance.player;
                InvItem item = InventoryManager.instance.getSelector().getSelectedInvItem();
                ComponentPacket packet = PacketUtils.createComponentPacket(player, ComponentType.EQUIP);
                ClientManager.instance.sendUDP(packet);

            }
        };

    }

    public void sendUDP(Object object) {
        client.sendUDP(object);
    }

    public void connect() {

        try {
            client.connect(5000, ip, 56098, 56099);
        } catch (IOException e) {
            Log.error(e.getMessage());
        }
        if (client.isConnected()) Log.info("Connected to Server!");

    }

    public void reconnect() {

        try {
            client.reconnect();
        } catch (IOException e) {
            Log.error(e.getMessage());
        }
        if (client.isConnected()) Log.info("Connected to Server!");

    }

    @Override
    public void update(float delta) {
        if (connected) {
            playerUpdateTimer60.update(delta);
            playerUpdateTimer10.update(delta);
        }
    }

    @Override
    public void delete() {
        super.delete();
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public int getID() {
        return client.getID();
    }

    public Client getClient() {
        return client;
    }

    public void sendTCP(Object object) {
        client.sendTCP(object);
    }

}
