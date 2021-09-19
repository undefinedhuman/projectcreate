package de.undefinedhuman.projectcreate.game.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.utils.NetworkConstants;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.Manager;
import de.undefinedhuman.projectcreate.engine.utils.Timer;

import java.io.IOException;

public class ClientManager extends Manager {

    private static volatile ClientManager instance;
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int TCP_PORT = NetworkConstants.DEFAULT_TCP_PORT;
    private static final int UDP_PORT = NetworkConstants.DEFAULT_UDP_PORT;

    private final Client client;

    private Timer timer;

    private ClientManager() {
        client = new Client(NetworkConstants.WRITE_BUFFER_SIZE, NetworkConstants.OBJECT_BUFFER_SIZE);
        client.start();

        NetworkConstants.register(client);

        client.addListener(new Listener.QueuedListener(new ClientListener()) {
            @Override
            protected void queue(Runnable runnable) {
                Gdx.app.postRunnable(runnable);
            }
        });

        timer = new Timer(0.2f, client::updateReturnTripTime);
    }

    public void connect() {
        try {
            client.connect(NetworkConstants.NETWORK_TIME_OUT, IP_ADDRESS, TCP_PORT, UDP_PORT);
        } catch (IOException e) {
            Log.error("Error while connecting to server " + IP_ADDRESS + ":" + TCP_PORT + "/" + UDP_PORT);
        }
    }

    @Override
    public void update(float delta) {
        timer.update(delta);
    }

    @Override
    public void delete() {
        client.stop();
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public void sendTCP(Packet packet) {
        if(!client.isConnected())
            return;
        if(packet == null) {
            Log.debug("Error while sending TCP packet, packet is null!");
            return;
        }
        client.sendTCP(packet);
    }

    public float getLatency() {
        return client.getReturnTripTime() / 1000f;
    }

    public int getReturnTime() {
        return client.getReturnTripTime();
    }

    public void sendUDP(Object object) {
        if(!client.isConnected())
            return;
        client.sendUDP(object);
    }

    public static ClientManager getInstance() {
        if(instance != null)
            return instance;
        synchronized (ClientManager.class) {
            if (instance == null)
                instance = new ClientManager();
        }
        return instance;
    }

}
