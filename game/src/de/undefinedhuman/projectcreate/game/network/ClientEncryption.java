package de.undefinedhuman.projectcreate.game.network;

import de.undefinedhuman.projectcreate.engine.log.Log;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class ClientEncryption {

    private static volatile ClientEncryption instance;

    private KeyFactory rsaGenerator;
    private KeyGenerator aesGenerator;
    private Key aesKey;
    private PublicKey serverKey;
    private Cipher encryptionRSACipher;
    private Cipher encryptionAESCipher;
    private Cipher decryptionAESCipher;

    private ClientEncryption() {
        try {
            rsaGenerator = KeyFactory.getInstance("RSA");
            aesGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            Log.crash("Error while creating key generator instances");
        }
    }

    public void init(byte[] publicKey) {
        if(rsaGenerator == null) return;
        try {
            this.serverKey = rsaGenerator.generatePublic(new X509EncodedKeySpec(publicKey));
        } catch (InvalidKeySpecException e) {
            Log.crash("Error while parsing public key from server");
        }
        try {
            encryptionRSACipher = Cipher.getInstance("RSA");
            encryptionRSACipher.init(Cipher.ENCRYPT_MODE, serverKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            Log.error("Error while creating rsa cipher", ex);
        }
        aesKey = generateAESKey();
        try {
            encryptionAESCipher = Cipher.getInstance("AES");
            encryptionAESCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            Log.error("Error while creating aes cipher", ex);
        }
        try {
            decryptionAESCipher = Cipher.getInstance("AES");
            decryptionAESCipher.init(Cipher.DECRYPT_MODE, aesKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            Log.error("Error while creating aes cipher", ex);
        }
    }

    public Cipher getAESEncryptionCipher() {
        return encryptionAESCipher;
    }

    public Cipher getRSAEncryptionCipher() {
        return encryptionRSACipher;
    }

    public Cipher getRSADecryptionCipher() {
        return decryptionAESCipher;
    }

    public Key getAesKey() {
        return aesKey;
    }

    private Key generateAESKey() {
        if(aesGenerator == null) return null;
        SecureRandom secureRandom = null;
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            Log.crash("Error while creating secure random instance, for generating aes key.");
        }
        if(secureRandom == null) return null;
        aesGenerator.init(128, secureRandom);
        return aesGenerator.generateKey();
    }

    public static ClientEncryption getInstance() {
        if(instance != null)
            return instance;
        synchronized (ClientEncryption.class) {
            if (instance == null)
                instance = new ClientEncryption();
        }
        return instance;
    }

}
