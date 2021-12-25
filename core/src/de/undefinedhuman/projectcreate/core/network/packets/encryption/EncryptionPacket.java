package de.undefinedhuman.projectcreate.core.network.packets.encryption;

import de.undefinedhuman.projectcreate.core.network.Packet;

import javax.crypto.Cipher;

public class EncryptionPacket implements Packet {

    public static final int INIT = 0x00;
    public static final int PUBLIC_KEY = 0x01;
    public static final int CLIENT_KEY = 0x02;
    public static final int SESSION_ID = 0x03;

    public int code;
    public String data;

    public static EncryptionPacket serialize(Cipher encryptionCipher, int code, String data) {
        EncryptionPacket packet = new EncryptionPacket();
        packet.code = code;
        if(encryptionCipher != null)
            data = EncryptionUtils.encodeBase64String(EncryptionUtils.encryptData(encryptionCipher, data));
        packet.data = data;
        return packet;
    }

}
