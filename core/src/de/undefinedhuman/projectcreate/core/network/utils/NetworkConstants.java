package de.undefinedhuman.projectcreate.core.network.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.packets.ping.PingPacket;

public class NetworkConstants {

    public static final int WRITE_BUFFER_SIZE = 65536;
    public static final int OBJECT_BUFFER_SIZE = 8192 * 4;

    public static final int DEFAULT_TCP_PORT = 21000;
    public static final int DEFAULT_UDP_PORT = 21001;

    public static final int NETWORK_TIME_OUT = 5000;

    public static void registerPackets(EndPoint endpoint) {
        registerPackets(endpoint.getKryo(),
                PingPacket.class
        );
    }

    @SafeVarargs
    private static void registerPackets(Kryo kryo, Class<? extends Packet>... packets) {
        for(Class<? extends Packet> packet : packets)
            kryo.register(packet);
    }

}
