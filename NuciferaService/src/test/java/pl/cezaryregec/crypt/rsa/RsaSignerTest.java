package pl.cezaryregec.crypt.rsa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.cezaryregec.crypt.keys.RsaKeySupplier;
import pl.cezaryregec.exception.APIException;
import pl.cezaryregec.logger.ApplicationLogger;
import pl.cezaryregec.logger.SecurityLogger;

import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class RsaSignerTest {
    private final RsaSigner rsaSigner;
    private final RsaVerifier rsaVerifier;

    public RsaSignerTest() throws GeneralSecurityException {
        ApplicationLogger applicationLogger = mock(ApplicationLogger.class);
        SecurityLogger securityLogger = mock(SecurityLogger.class);
        RsaKeySupplier rsaKeySupplier = new RsaKeySupplier(applicationLogger, securityLogger);
        this.rsaSigner = new RsaSigner(rsaKeySupplier);
        this.rsaVerifier = new RsaVerifier();
    }

    @Test
    @DisplayName("Should sign String")
    public void shouldSignString() throws GeneralSecurityException, APIException {
        String plain = "Lorem Ipsum 123";
        String signature = rsaSigner.sign(plain);

        boolean verification = rsaVerifier.verify(plain, signature);

        assertTrue(verification);
    }


    @Test
    @DisplayName("Should sign byte[] array")
    public void shouldByteArray() throws GeneralSecurityException, APIException {
        byte[] plain = {76, 111, 114, 101, 109, 32, 105, 112, 115, 117, 109, 32, 100, 111, 108, 111, 114, 32, 115, 105, 116, 32, 97, 109, 101, 116};
        byte[] signature = rsaSigner.sign(plain);

        boolean verification = rsaVerifier.verify(plain, signature);

        assertTrue(verification);
    }

    @Test
    @DisplayName("Should fail verification")
    public void shouldFailVerification() throws GeneralSecurityException, APIException {
        String plain = "Lorem Ipsum";
        String signature = rsaSigner.sign(plain);

        boolean verification = rsaVerifier.verify(plain.toLowerCase(), signature);

        assertFalse(verification);
    }
}
