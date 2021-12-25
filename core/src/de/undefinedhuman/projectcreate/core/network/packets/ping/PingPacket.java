package de.undefinedhuman.projectcreate.core.network.packets.ping;

import de.undefinedhuman.projectcreate.core.network.Packet;

public class PingPacket implements Packet {
    public long time;

    public static PingPacket serialize(long time) {
        PingPacket packet = new PingPacket();
        packet.time = time;
        return packet;
    }

}
