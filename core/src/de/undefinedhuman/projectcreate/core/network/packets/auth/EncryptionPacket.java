package de.undefinedhuman.projectcreate.core.network.packets.auth;

import de.undefinedhuman.projectcreate.core.network.Packet;

import javax.crypto.Cipher;

public class EncryptionPacket implements Packet {

    public static final int INIT = 0x00;
    public static final int PUBLIC_KEY = 0x01;
    public static final int CLIENT_KEY = 0x02;
    public static final int SESSION_ID = 0x03;
    public static final int CHARACTER_SELECTION = 0x04;
    public static final int CHARACTER_RESPONSE = 0x05;

    private int code;
    private String data;

    private EncryptionPacket() {}

    public static EncryptionPacket serialize(Cipher encryptionCipher, int code, String data) {
        EncryptionPacket packet = new EncryptionPacket();
        packet.code = code;
        if(encryptionCipher != null)
            data = EncryptionUtils.encodeBase64String(EncryptionUtils.encryptData(encryptionCipher, data));
        packet.data = data;
        return packet;
    }

    public static String parse(Cipher decryptionCipher, EncryptionPacket packet) {
        if(decryptionCipher != null)
            return EncryptionUtils.decryptData(decryptionCipher, EncryptionUtils.decodeBase64String(packet.data));
        return packet.data;
    }

    public int getCode() {
        return code;
    }

    public String getData() {
        return data;
    }

}
