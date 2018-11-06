package pl.cezaryregec.crypt;

import com.google.inject.Inject;
import pl.cezaryregec.crypt.keys.RsaKeySupplier;
import pl.cezaryregec.exception.APIException;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.Signature;
import java.util.Base64;
import java.util.Optional;

public class RsaSigner implements AsymmetricSigner {
    private final RsaKeySupplier keySupplier;

    @Inject
    public RsaSigner(RsaKeySupplier keySupplier) {
        this.keySupplier = keySupplier;
    }

    @Override
    public String sign(String input) throws APIException {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        byte[] result = sign(bytes);
        return Base64.getEncoder().encodeToString(result);
    }

    @Override
    public byte[] sign(byte[] input) throws APIException {
        KeyPair keyPair = getKeyPair();
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(keyPair.getPrivate());
            signature.update(input);

            return signature.sign();
        } catch (Exception ex) {
            throw new APIException(ex);
        }
    }

    private KeyPair getKeyPair() throws APIException {
        Optional<KeyPair> keyPair = keySupplier.get();
        if (!keyPair.isPresent()) {
            throw new APIException("No RSA key is available, cannot sign");
        }
        return keyPair.get();
    }
}
