package de.undefinedhuman.projectcreate.core.network.encryption;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.engine.utils.ds.Tuple;

import java.security.PublicKey;

public class EncryptionRequest implements Packet {

    public String asymmetricPublicKey;
    public String verifyToken;

    private EncryptionRequest() {}

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static EncryptionRequest serialize(PublicKey asymmetricPublicKey, byte[] verifyToken) {
        EncryptionRequest packet = new EncryptionRequest();
        packet.asymmetricPublicKey = EncryptionUtils.encodeBase64String(asymmetricPublicKey.getEncoded());
        packet.verifyToken = EncryptionUtils.encodeBase64String(verifyToken);
        return packet;
    }

    public static Tuple<byte[], byte[]> parse(EncryptionRequest packet) {
        byte[] publicKeyBytes = EncryptionUtils.decodeBase64String(packet.asymmetricPublicKey);
        byte[] verifyTokenBytes = EncryptionUtils.decodeBase64String(packet.verifyToken);
        return new Tuple<>(publicKeyBytes, verifyTokenBytes);
    }

}
