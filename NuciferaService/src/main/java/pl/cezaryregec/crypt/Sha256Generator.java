package pl.cezaryregec.crypt;

import pl.cezaryregec.exception.APIException;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Sha256Generator implements HashGenerator {

    @Override
    public String encode(@NotNull String text) throws APIException {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(encode(bytes));
    }

    @Override
    public byte[] encode(@NotNull byte[] bytes) throws APIException {
        MessageDigest digest;
        byte[] result;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            result = digest.digest(bytes);
            return result;
        } catch (NoSuchAlgorithmException ex) {
            throw new APIException(ex);
        }
    }
}
