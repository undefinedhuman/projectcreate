package de.undefinedhuman.projectcreate.core.network.encryption;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;

import javax.crypto.Cipher;

public class SessionPacket implements Packet {

    public String data;

    private SessionPacket() {}

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static SessionPacket serialize(Cipher encryptionCipher, String sessionID) {
        SessionPacket packet = new SessionPacket();
        packet.data = EncryptionUtils.encodeBase64String(EncryptionUtils.encryptData(encryptionCipher, sessionID));
        return packet;
    }

    public static String parse(Cipher decryptionCipher, SessionPacket packet) {
        return EncryptionUtils.decryptData(decryptionCipher, EncryptionUtils.decodeBase64String(packet.data));
    }

}
