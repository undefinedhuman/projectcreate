package de.undefinedhuman.projectcreate.game.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import de.undefinedhuman.projectcreate.core.ecs.equip.EquipComponent;
import de.undefinedhuman.projectcreate.core.ecs.mouse.AngleComponent;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.Manager;
import de.undefinedhuman.projectcreate.game.camera.CameraManager;
import de.undefinedhuman.projectcreate.game.entity.Entity;
import de.undefinedhuman.projectcreate.game.inventory.InvItem;
import de.undefinedhuman.projectcreate.game.inventory.player.Selector;
import de.undefinedhuman.projectcreate.game.network.packets.PacketManager;
import de.undefinedhuman.projectcreate.game.network.packets.entity.ComponentPacket;
import de.undefinedhuman.projectcreate.game.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.game.utils.Timer;
import de.undefinedhuman.projectcreate.game.utils.Tools;

import java.io.IOException;

public class ClientManager extends Manager {

    private static volatile ClientManager instance;

    public String ip = "127.0.0.1";
    public boolean connected = false;
    private Timer playerUpdateTimer60, playerUpdateTimer10;
    private Client client;

    private ClientManager() {

        client = new Client(1048576, 1048576);
        client.start();

        PacketManager.register(client);

        client.addListener(new Listener.QueuedListener(new ClientListener()) {
            protected void queue(Runnable runnable) {
                Gdx.app.postRunnable(runnable);
            }
        });

        playerUpdateTimer60 = new Timer(0.015f, true, () -> {
            Entity player = GameManager.instance.player;
            ((AngleComponent) player.getComponent(AngleComponent.class)).mousePos = Tools.getMouseCoordsInWorldSpace(CameraManager.gameCamera);
            ComponentPacket packet = PacketUtils.createComponentPacket(player, AngleComponent.class);
            ClientManager.getInstance().sendUDP(packet);
        });

        playerUpdateTimer10 = new Timer(0.1f, true, () -> {
            Entity player = GameManager.instance.player;
            InvItem item = Selector.getInstance().getSelectedInvItem();
            ComponentPacket packet = PacketUtils.createComponentPacket(player, EquipComponent.class);
            ClientManager.getInstance().sendUDP(packet);
        });

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

    public static ClientManager getInstance() {
        if (instance == null) {
            synchronized (ClientManager.class) {
                if (instance == null)
                    instance = new ClientManager();
            }
        }
        return instance;
    }

}
