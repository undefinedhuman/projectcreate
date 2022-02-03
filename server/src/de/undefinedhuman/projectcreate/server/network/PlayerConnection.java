package de.undefinedhuman.projectcreate.server.network;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.engine.log.Log;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class PlayerConnection extends Connection {
    public long worldID = -1;
    private Cipher encryptionCipher;
    private Cipher decryptionCipher;

    public void init(byte[] clientKey) {
        Key symmetricClientKey = new SecretKeySpec(clientKey, "AES");
        try {
            decryptionCipher = Cipher.getInstance("AES");
            decryptionCipher.init(Cipher.DECRYPT_MODE, symmetricClientKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            Log.error("Error while initializing client aes decryption cipher with given client key!", ex);
            decryptionCipher = null;
            return;
        }
        try {
            encryptionCipher = Cipher.getInstance("AES");
            encryptionCipher.init(Cipher.ENCRYPT_MODE, symmetricClientKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            Log.error("Error while initializing client aes encryption cipher with given client key!", ex);
            encryptionCipher = null;
        }
    }

    public Cipher getEncryptionCipher() {
        return encryptionCipher;
    }

    public Cipher getDecryptionCipher() {
        return decryptionCipher;
    }

}
