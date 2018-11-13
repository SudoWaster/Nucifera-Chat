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
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AesDecryptorTest {
    private final AesDecryptor aesDecryptor;

    public AesDecryptorTest() {
        aesDecryptor = new AesDecryptor();
    }

    @Test
    @DisplayName("Should decrypt String using given key")
    public void shouldDecryptString() throws InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
        String encrypted = "NQqlx5ZWACoph7/St3gbMQ==";
        String key = "1234567890123456";
        String expected = "Lorem Ipsum";

        String decrypted = aesDecryptor.decrypt(encrypted, key);

        assertEquals(expected, decrypted);
    }

    @Test
    @DisplayName("Should decrypt bytes using given key")
    public void shouldDecryptBytes() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        byte[] encrypted = { 53, 10, -91, -57, -106, 86, 0, 42, 41, -121, -65, -46, -73, 120, 27, 49 };
        byte[] key = {49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54 };
        byte[] expected = { 76, 111, 114, 101, 109, 32, 73, 112, 115, 117, 109 };

        byte[] decrypted = aesDecryptor.decrypt(encrypted, key);

        assertArrayEquals(expected, decrypted);
    }

    @Test
    @DisplayName("Should fail with wrong key used")
    public void shouldFailWithWrongKey() {
        byte[] encrypted = { 53, 10, -91, -57, -106, 86, 0, 42, 41, -121, -65, -46, -73, 120, 27, 49 };
        byte[] wrongKey = {49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 55 };
        byte[] invalidKey = {49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53 };

        assertThrows(BadPaddingException.class, () -> aesDecryptor.decrypt(encrypted, wrongKey));
        assertThrows(InvalidKeyException.class, () -> aesDecryptor.decrypt(encrypted, invalidKey));
    }
}
