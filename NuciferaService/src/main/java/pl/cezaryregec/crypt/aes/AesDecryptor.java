package pl.cezaryregec.crypt.aes;

import pl.cezaryregec.crypt.SymmetricDecryptor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AesDecryptor implements SymmetricDecryptor {
    @Override
    public String decrypt(String input, String challenge) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        byte[] bytes = Base64.getDecoder().decode(input);
        byte[] key = challenge.getBytes(StandardCharsets.UTF_8);
        byte[] result = decrypt(bytes, key);
        return new String(result, StandardCharsets.UTF_8);
    }

    @Override
    public byte[] decrypt(byte[] input, byte[] challenge) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec keySpec = new SecretKeySpec(challenge, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(input);
    }
}
