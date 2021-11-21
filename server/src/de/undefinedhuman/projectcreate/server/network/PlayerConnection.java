package de.undefinedhuman.projectcreate.server.network;

import com.esotericsoftware.kryonet.Connection;

import javax.crypto.Cipher;

public class PlayerConnection extends Connection {
    public long worldID = -1;
    public Cipher decryptionCipher;
}
