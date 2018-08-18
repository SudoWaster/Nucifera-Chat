package pl.cezaryregec.crypt;

import pl.cezaryregec.ApplicationLogger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Sha256Generator implements HashGenerator {

    @Override
    public String encode(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(encode(bytes));
    }

    @Override
    public byte[] encode(byte[] bytes) {
        MessageDigest digest;
        byte[] result = new byte[0];
        try {
            digest = MessageDigest.getInstance("SHA-256");
            result = digest.digest(bytes);
        } catch (NoSuchAlgorithmException ex) {
            ApplicationLogger.log(ex);
        }
        return result;
    }
}
