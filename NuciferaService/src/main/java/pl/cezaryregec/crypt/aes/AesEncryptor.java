package pl.cezaryregec.crypt.aes;

import pl.cezaryregec.crypt.SymmetricEncryptor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AesEncryptor implements SymmetricEncryptor {
    @Override
    public String encrypt(String input, String challenge) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        byte[] key = challenge.getBytes(StandardCharsets.UTF_8);
        byte[] result = encrypt(bytes, key);
        return Base64.getEncoder().encodeToString(result);
    }

    @Override
    public byte[] encrypt(byte[] input, byte[] challenge) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec keySpec = new SecretKeySpec(challenge, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(input);
    }
}
