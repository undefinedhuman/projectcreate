package de.undefinedhuman.projectcreate.core.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import de.undefinedhuman.projectcreate.core.network.packets.PingPacket;

public class NetworkConstants {

    public static final int WRITE_BUFFER_SIZE = 65536;
    public static final int OBJECT_BUFFER_SIZE = 8192 * 4;

    public static final int TCP_PORT = 8000;
    public static final int UDP_PORT = 8001;

    public static void register(EndPoint endpoint) {
        Kryo kryo = endpoint.getKryo();
        kryo.register(PingPacket.class);
    }
}
