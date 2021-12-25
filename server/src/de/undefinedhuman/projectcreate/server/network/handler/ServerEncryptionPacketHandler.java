package de.undefinedhuman.projectcreate.server.network.handler;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.packets.encryption.EncryptionPacket;
import de.undefinedhuman.projectcreate.core.network.packets.encryption.EncryptionPacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.encryption.EncryptionUtils;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.server.network.PlayerConnection;
import de.undefinedhuman.projectcreate.server.network.ServerEncryption;
import de.undefinedhuman.projectcreate.server.network.session.SessionManager;

import java.util.Arrays;
import java.util.HashMap;

public class ServerEncryptionPacketHandler extends EncryptionPacketHandler {

    private HashMap<Connection, byte[]> verifyTokens = new HashMap<>();

    public ServerEncryptionPacketHandler() {
        registerHandlerFunction(EncryptionPacket.INIT, (connection, packet) -> handleInit(connection));
        registerHandlerFunction(EncryptionPacket.CLIENT_KEY, this::handleClientKey);
    }

    private void handleInit(Connection connection) {
        if(verifyTokens.containsKey(connection))
            return;
        byte[] verifyToken = ServerEncryption.getInstance().generateVerifyToken();
        String encodedVerifyToken = EncryptionUtils.encodeBase64String(verifyToken);
        String encodedPublicKey = EncryptionUtils.encodeBase64String(ServerEncryption.getInstance().getPublicKey().getEncoded());
        verifyTokens.put(connection, verifyToken);
        connection.sendTCP(EncryptionPacket.serialize(null, EncryptionPacket.PUBLIC_KEY, encodedVerifyToken + ":" + encodedPublicKey));
    }

    private void handleClientKey(Connection connection, EncryptionPacket packet) {
        if(!verifyTokens.containsKey(connection))
            return;
        byte[] decryptedString = EncryptionUtils.decodeBase64String(packet.data);
        String decryptedData = EncryptionUtils.decryptData(ServerEncryption.getInstance().getDecryptionCipher(), decryptedString);
        String[] data = decryptedData.split(":");
        if(data.length != 2) {
            Log.error("Received client key data does not follow given specifications!");
            return;
        }
        byte[] verifyToken = EncryptionUtils.decodeBase64String(data[0]);
        byte[] clientKey = EncryptionUtils.decodeBase64String(data[1]);
        if(!Arrays.equals(verifyTokens.get(connection), verifyToken)) {
            Log.error("Received verify token does not match the one generated earlier!");
            return;
        } else verifyTokens.remove(connection);
        if(!(connection instanceof PlayerConnection)) return;
        PlayerConnection playerConnection = (PlayerConnection) connection;
        playerConnection.init(clientKey);

        String sessionID = EncryptionUtils.encodeBase64String(ServerEncryption.getInstance().generateSessionID());
        SessionManager.getInstance().registerSession(sessionID);
        connection.sendTCP(EncryptionPacket.serialize(playerConnection.getEncryptionCipher(), EncryptionPacket.SESSION_ID, sessionID));

    }

}
