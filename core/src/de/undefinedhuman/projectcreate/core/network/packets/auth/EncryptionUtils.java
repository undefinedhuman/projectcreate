package de.undefinedhuman.projectcreate.core.network.packets.auth;

import com.badlogic.gdx.utils.Base64Coder;
import de.undefinedhuman.projectcreate.engine.log.Log;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.nio.charset.StandardCharsets;

public class EncryptionUtils {

    public static final Base64Coder.CharMap BASE64_CHAR_MAP = Base64Coder.regularMap;

    public static String encodeBase64String(byte[] data) {
        return String.valueOf(Base64Coder.encode(data, BASE64_CHAR_MAP));
    }

    public static byte[] decodeBase64String(String data) {
        return Base64Coder.decode(data, BASE64_CHAR_MAP);
    }

    public static byte[] encryptData(Cipher encryptionCipher, String data) {
        if(encryptionCipher == null)
            return null;
        try {
            return encryptionCipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            Log.error("Error while encrypting data!", ex);
        }
        return null;
    }

    public static String decryptData(Cipher decryptionCipher, byte[] data) {
        if(decryptionCipher == null)
            return "";
        try {
            byte[] decryptedData = decryptionCipher.doFinal(data);
            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            Log.error("Error while encrypting data!", ex);
        }
        return "";
    }

}
