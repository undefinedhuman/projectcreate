package de.undefinedhuman.projectcreate.core.network.authentication;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.encryption.EncryptionUtils;
import de.undefinedhuman.projectcreate.engine.utils.ds.Tuple;

import javax.crypto.Cipher;

public class LoginRequest implements Packet {

    public String data;

    private LoginRequest() {}

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static LoginRequest serialize(Cipher encryptionCipher, String name, String sessionID) {
        LoginRequest packet = new LoginRequest();
        packet.data = EncryptionUtils.encodeBase64String(EncryptionUtils.encryptData(encryptionCipher, sessionID + ":" + name));
        return packet;
    }

    public static Tuple<String, String> parse(Cipher decryptionCipher, LoginRequest packet) {
        String decryptedData = EncryptionUtils.decryptData(decryptionCipher, EncryptionUtils.decodeBase64String(packet.data));
        String[] data = decryptedData.split(":");
        if(data.length != 2)
            return null;
        return new Tuple<>(data[0], data[1]);
    }

}
