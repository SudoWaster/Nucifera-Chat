package pl.cezaryregec.crypt.rsa;

import com.google.inject.Inject;
import pl.cezaryregec.crypt.AsymmetricDecryptor;
import pl.cezaryregec.crypt.keys.RsaKeySupplier;
import pl.cezaryregec.exception.APIException;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Base64;
import java.util.Optional;

public class RsaDecryptor implements AsymmetricDecryptor {
    private final RsaKeySupplier keySupplier;

    @Inject
    public RsaDecryptor(RsaKeySupplier keySupplier) {
        this.keySupplier = keySupplier;
    }

    @Override
    public String decrypt(String input) throws APIException {
        byte[] bytes = Base64.getDecoder().decode(input);
        byte[] result = decrypt(bytes);
        return new String(result, StandardCharsets.UTF_8);
    }

    @Override
    public byte[] decrypt(byte[] input) throws APIException {
        KeyPair keyPair = getKeyPair();
        try {
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());

            return decryptCipher.doFinal(input);
        } catch (Exception ex) {
            throw new APIException(ex);
        }
    }

    private KeyPair getKeyPair() throws APIException {
        Optional<KeyPair> keyPair = keySupplier.get();
        if (!keyPair.isPresent()) {
            throw new APIException("No RSA key is available, cannot decrypt");
        }
        return keyPair.get();
    }
}
