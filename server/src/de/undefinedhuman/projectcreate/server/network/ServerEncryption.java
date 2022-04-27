package de.undefinedhuman.projectcreate.server.network;

import de.undefinedhuman.projectcreate.engine.log.Log;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

public class ServerEncryption {

    private static volatile ServerEncryption instance;

    private static final int VERIFY_TOKEN_LENGTH_BYTES = 16;
    private static final int SESSION_ID_LENGTH_BYTES = 32;
    private static final int RSA_KEY_LENGTH = 4096;

    private SecureRandom secureRandom;
    private KeyPair rsaKeys;
    private Cipher decryptionRSACipher;

    private ServerEncryption() {
        this.secureRandom = getSecureRandom();
        rsaKeys = generateRSAKeys();
        try {
            decryptionRSACipher = Cipher.getInstance("RSA");
            decryptionRSACipher.init(Cipher.DECRYPT_MODE, rsaKeys.getPrivate());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            Log.error("Error while creating rsa cipher", ex);
        }
    }

    public byte[] generateVerifyToken() {
        if(secureRandom == null)
            return null;
        byte[] bytes = new byte[VERIFY_TOKEN_LENGTH_BYTES];
        secureRandom.nextBytes(bytes);
        return bytes;
    }

    public byte[] generateSessionID() {
        if(secureRandom == null)
            return null;
        byte[] bytes = new byte[SESSION_ID_LENGTH_BYTES];
        secureRandom.nextBytes(bytes);
        return bytes;
    }

    public PublicKey getPublicKey() {
        return rsaKeys.getPublic();
    }

    public Cipher getDecryptionCipher() {
        return decryptionRSACipher;
    }

    private SecureRandom getSecureRandom() {
        try {
            return SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            Log.crash("Error while creating strong SecureRandom instance");
        }
        return null;
    }

    private KeyPair generateRSAKeys() {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            Log.crash("Error while creating RSA generator instance");
        }
        if(keyPairGenerator == null) return null;
        SecureRandom random = new SecureRandom();
        keyPairGenerator.initialize(RSA_KEY_LENGTH, random);
        return keyPairGenerator.generateKeyPair();
    }

    public static ServerEncryption getInstance() {
        if(instance != null)
            return instance;
        synchronized (ServerEncryption.class) {
            if (instance == null)
                instance = new ServerEncryption();
        }
        return instance;
    }

}
