package pl.cezaryregec.crypt.keys;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class RsaKeyPairGeneratorFactory {

    public KeyPairGenerator create() throws NoSuchAlgorithmException {
        return KeyPairGenerator.getInstance("RSA");
    }
}
