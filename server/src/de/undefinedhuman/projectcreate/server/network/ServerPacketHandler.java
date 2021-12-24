package de.undefinedhuman.projectcreate.server.network;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.encryption.*;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.ds.Tuple;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

public class ServerPacketHandler implements PacketHandler {

    private HashMap<String, Long> sessionIDs = new HashMap<>();

    private HashMap<Connection, byte[]> verifyTokens = new HashMap<>();

    @Override
    public void handle(Connection connection, InitPacket packet) {
        PlayerConnection playerConnection = (PlayerConnection) connection;
        if(playerConnection.decryptionCipher != null || verifyTokens.containsKey(connection))
            return;
        byte[] verifyToken = ServerEncryption.getInstance().generateVerifyToken();
        verifyTokens.put(connection, verifyToken);
        connection.sendTCP(EncryptionRequest.serialize(ServerEncryption.getInstance().getPublicKey(), verifyToken));
    }

    @Override
    public void handle(Connection connection, EncryptionResponse packet) {
        PlayerConnection playerConnection = (PlayerConnection) connection;
        if(playerConnection.decryptionCipher != null || !verifyTokens.containsKey(connection))
            return;
        Tuple<byte[], byte[]> clientKeys = EncryptionResponse.parse(ServerEncryption.getInstance().getDecryptionCipher(), packet);
        if(clientKeys == null)
            return;
        if(!Arrays.equals(verifyTokens.get(connection), clientKeys.getU()))
            return;
        verifyTokens.remove(connection);
        Key symmetricClientKey = new SecretKeySpec(clientKeys.getT(), "AES");
        try {
            playerConnection.decryptionCipher = Cipher.getInstance("AES");
            playerConnection.decryptionCipher.init(Cipher.DECRYPT_MODE, symmetricClientKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            Log.error("Error while creating temporary client aes cipher", ex);
            playerConnection.decryptionCipher = null;
            return;
        }
        Cipher clientEncryptionCipher;
        try {
            clientEncryptionCipher = Cipher.getInstance("AES");
            clientEncryptionCipher.init(Cipher.ENCRYPT_MODE, symmetricClientKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            Log.error("Error while creating temporary client aes cipher", ex);
            return;
        }
        String sessionID = EncryptionUtils.encodeBase64String(ServerEncryption.getInstance().generateSessionID());
        sessionIDs.put(sessionID, -1L);
        connection.sendTCP(SessionPacket.serialize(clientEncryptionCipher, sessionID));
    }

}
