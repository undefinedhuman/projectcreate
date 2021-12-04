package de.undefinedhuman.projectcreate.core.network.encryption;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.engine.utils.ds.Tuple;

import javax.crypto.Cipher;

public class EncryptionResponse implements Packet {

    public String data;

    private EncryptionResponse() {}

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static EncryptionResponse serialize(Cipher cipher, byte[] symmetricKey, byte[] verifyToken) {
        EncryptionResponse packet = new EncryptionResponse();
        String base64SymmetricKey = EncryptionUtils.encodeBase64String(symmetricKey);
        String base64VerifyToken = EncryptionUtils.encodeBase64String(verifyToken);
        packet.data = EncryptionUtils.encodeBase64String(EncryptionUtils.encryptData(cipher, base64SymmetricKey + ":" + base64VerifyToken));
        return packet;
    }

    public static Tuple<byte[], byte[]> parse(Cipher decryptionCipher, EncryptionResponse packet) {
        String encryptedData = EncryptionUtils.decryptData(decryptionCipher, EncryptionUtils.decodeBase64String(packet.data));
        String[] encryptedKeys = encryptedData.split(":");
        if(encryptedKeys.length != 2)
            return null;
        return new Tuple<>(EncryptionUtils.decodeBase64String(encryptedKeys[0]), EncryptionUtils.decodeBase64String(encryptedKeys[1]));
    }

}
