package pl.cezaryregec.crypt.aes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AesEncryptorTest {
    private final AesEncryptor aesEncryptor;

    public AesEncryptorTest() {
        aesEncryptor = new AesEncryptor();
    }

    @Test
    @DisplayName("Should encrypt String using given key")
    public void shouldEncryptString() throws InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
        String plain = "Lorem Ipsum";
        String key = "1234567890123456";
        String expected = "NQqlx5ZWACoph7/St3gbMQ==";

        String encrypted = aesEncryptor.encrypt(plain, key);

        assertEquals(expected, encrypted);
    }

    @Test
    @DisplayName("Should encrypt byte array using given key")
    public void shouldEncryptBytes() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        byte[] plain = { 76, 111, 114, 101, 109, 32, 73, 112, 115, 117, 109 };
        byte[] key = {49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54 };
        byte[] expected = { 53, 10, -91, -57, -106, 86, 0, 42, 41, -121, -65, -46, -73, 120, 27, 49 };

        byte[] encrypted = aesEncryptor.encrypt(plain, key);

        assertArrayEquals(expected, encrypted);
    }
}
