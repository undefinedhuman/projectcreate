package de.undefinedhuman.projectcreate.core.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
    public static final int TCP_PORT = 8000;
    public static final int UDP_PORT = 8001;

    public static void register(EndPoint endpoint) {
        Kryo kryo = endpoint.getKryo();
        kryo.register(LoginPacket.class);
        kryo.register(ServerClosedPacket.class);
    }
}
