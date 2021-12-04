package de.undefinedhuman.projectcreate.core.network.encryption;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;

public class EncryptionPacket implements Packet {

    public static int INIT = 0x00;

    private int packetID;
    private String data;

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static void parse(EncryptionPacketRequest request) {

    }


}
