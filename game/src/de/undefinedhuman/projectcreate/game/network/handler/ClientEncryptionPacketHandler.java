package de.undefinedhuman.projectcreate.game.network.handler;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.packets.encryption.EncryptionPacket;
import de.undefinedhuman.projectcreate.core.network.packets.encryption.EncryptionPacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.encryption.EncryptionUtils;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.game.Main;
import de.undefinedhuman.projectcreate.game.network.ClientEncryption;
import de.undefinedhuman.projectcreate.game.network.ClientManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameScreen;

public class ClientEncryptionPacketHandler extends EncryptionPacketHandler {

    public ClientEncryptionPacketHandler() {
        registerHandlerFunction(EncryptionPacket.PUBLIC_KEY, this::handlePublicKey);
        registerHandlerFunction(EncryptionPacket.SESSION_ID, this::handleSessionID);
    }

    public void handlePublicKey(Connection connection, EncryptionPacket packet) {
        String[] data = packet.data.split(":");
        if(data.length != 2) {
            Log.error("Received public key data does not follow given specifications!");
            return;
        }
        String verifyToken = data[0];
        byte[] publicKey = EncryptionUtils.decodeBase64String(data[1]);
        ClientEncryption.getInstance().init(publicKey);
        String combinedKeyWithToken = verifyToken + ":" + ClientEncryption.getInstance().getAESKeyAsString();
        connection.sendTCP(EncryptionPacket.serialize(ClientEncryption.getInstance().getRSAEncryptionCipher(), EncryptionPacket.CLIENT_KEY, combinedKeyWithToken));
    }

    public void handleSessionID(Connection connection, EncryptionPacket packet) {
        ClientManager.getInstance().currentSessionID = EncryptionUtils.decryptData(ClientEncryption.getInstance().getDecryptionAESCipher(), EncryptionUtils.decodeBase64String(packet.data));
        Main.getInstance().setScreen(GameScreen.getInstance());
    }

}
