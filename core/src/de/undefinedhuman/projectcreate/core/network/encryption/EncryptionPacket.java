package de.undefinedhuman.projectcreate.core.network.encryption;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.engine.utils.ds.Tuple;

public class EncryptionPacket implements Packet {

    public static final int INIT = 0x00;
    public static final int SERVER_PUBLIC_KEY_EXCHANGE = 0x01;
    public static final int CLIENT_KEY_EXCHANGE = 0x02;

    private int code;
    private String data;

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static EncryptionPacket serialize(int code, String data) {
        EncryptionPacket packet = new EncryptionPacket();
        packet.code = code;
        packet.data = data;
        return packet;
    }

    public static Tuple<Integer, String> parse() {

    }

    public static EncryptionPacket createInitPacket() {
        return serialize(INIT, "");
    }

    public static EncryptionPacket createEncryption(String verifyToken, String publicToken) {
        return serialize(SERVER_PUBLIC_KEY_EXCHANGE, publicToken + ":" + verifyToken);
    }

}
