package pl.cezaryregec.crypt.rsa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.cezaryregec.crypt.keys.RsaKeySupplier;
import pl.cezaryregec.exception.APIException;
import pl.cezaryregec.logger.ApplicationLogger;
import pl.cezaryregec.logger.SecurityLogger;

import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class RsaDecryptorTest {
    private final RsaDecryptor rsaDecryptor;
    private final RsaEncryptor rsaEncryptor;

    public RsaDecryptorTest() throws GeneralSecurityException {
        ApplicationLogger applicationLogger = mock(ApplicationLogger.class);
        SecurityLogger securityLogger = mock(SecurityLogger.class);
        RsaKeySupplier rsaKeySupplier = new RsaKeySupplier(applicationLogger, securityLogger);
        this.rsaDecryptor = new RsaDecryptor(rsaKeySupplier);
        this.rsaEncryptor = new RsaEncryptor();
    }

    @Test
    @DisplayName("Should test for key creation (encrypt & decrypt)")
    public void shouldTestKeyCreation() throws GeneralSecurityException, APIException {
        String plainText = "Lorem Ipsum";
        String encrypted = rsaEncryptor.encrypt(plainText);

        String decrypted = rsaDecryptor.decrypt(encrypted);

        assertEquals(plainText, decrypted);
    }

    @Test
    @DisplayName("Should decrypt Base64 String")
    public void shouldDecryptBase64() throws APIException {
        String plainText = "Zażółć gęślą jaźń";
        String encrypted = "nx0rgoR7RXAtNMiyWH5iiDdBa2IirR4Uyyy1h1TcA1PvBYgh1PEBh3W9IS0Th3x7txa4AI/ck8/empIycc7wPYch42A3rD3WG4wcnb6z9DyX07V5PbZvAgYPNmC7qMrmiVW9Y7v3rC6D6SsGHg5j9vMxFWfRdv6o/spS5EKAek8QJO8binXJiCS/qThgnZ+lNQw/iV7Yjlq7RgTIpJ2OIdVBk5zDNcQGiN/Krbo7dc6FuEpiWqerl78OF1qdYuTxqWNdeU+bPK9vuqrhV4+kl9PRRQiDJdnvyio/rhGM6kHIaVSxbhuhdNnejGBbZnje+bnLq6DZwnqb4k6J+QLTvA==";

        String decrypted = rsaDecryptor.decrypt(encrypted);

        assertEquals(plainText, decrypted);
    }

    @Test
    @DisplayName("Should decrypt encrypted byte[] array")
    public void shouldDecryptByteArray() throws GeneralSecurityException, APIException {
        byte[] plain = {76, 111, 114, 101, 109, 32, 105, 112, 115, 117, 109, 32, 100, 111, 108, 111, 114, 32, 115, 105, 116, 32, 97, 109, 101, 116};
        byte[] encrypted = rsaEncryptor.encrypt(plain);

        byte[] decrypted = rsaDecryptor.decrypt(encrypted);

        assertArrayEquals(plain, decrypted);
    }
}
