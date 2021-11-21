package de.undefinedhuman.projectcreate.core.network.packets.entity.movement;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.encryption.EncryptionUtils;
import de.undefinedhuman.projectcreate.engine.utils.Utils;
import de.undefinedhuman.projectcreate.engine.utils.ds.Tuple;

import javax.crypto.Cipher;

public class MovementRequest implements Packet {

    public String data;

    private MovementRequest() {}

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static MovementRequest serialize(Cipher encryptionCipher, String sessionID, int direction) {
        String rawData = sessionID + ":" + direction;
        MovementRequest packet = new MovementRequest();
        packet.data = EncryptionUtils.encodeBase64String(EncryptionUtils.encryptData(encryptionCipher, rawData));
        return packet;
    }

    public static Tuple<String, Integer> parse(Cipher decryptionCipher, MovementRequest packet) {
        String decryptedData = EncryptionUtils.decryptData(decryptionCipher, EncryptionUtils.decodeBase64String(packet.data));
        String[] rawData = decryptedData.split(":");
        Integer direction;
        if(rawData.length != 2 || (direction = Utils.isInteger(rawData[1])) == null)
            return null;
        return new Tuple<>(rawData[0], direction);
    }

}
