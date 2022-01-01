package de.undefinedhuman.projectcreate.core.network.packets.input;

import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.packets.auth.EncryptionUtils;
import de.undefinedhuman.projectcreate.engine.log.Log;

import javax.crypto.Cipher;

public class InputPacket implements Packet {

    public static final int DIRECTION = 0x00;
    public static final int JUMP = 0x01;

    private int code;
    private String data;

    public static InputPacket createDirectionPacket(Cipher encryptionCipher, String sessionID, int direction) {
        return serialize(encryptionCipher, DIRECTION, sessionID + ":" + direction);
    }

    public static InputPacket createJumpPacket(Cipher encryptionCipher, String sessionID) {
        return serialize(encryptionCipher, JUMP, sessionID);
    }

    private static InputPacket serialize(Cipher encryptionCipher, int code, String data) {
        if(encryptionCipher == null) {
            Log.warn("No encryption cipher supplied while sending input packet!");
            return null;
        }
        InputPacket packet = new InputPacket();
        packet.code = code;
        packet.data = EncryptionUtils.encodeBase64String(EncryptionUtils.encryptData(encryptionCipher, data));
        return packet;
    }

    public static String parse(Cipher decryptionCipher, InputPacket packet) {
        if(decryptionCipher == null) {
            Log.warn("No decryption cipher supplied while parsing input packet!");
            return null;
        }
        return EncryptionUtils.decryptData(decryptionCipher, EncryptionUtils.decodeBase64String(packet.data));
    }

    public int getCode() {
        return code;
    }

    public String getData() {
        return data;
    }

}
