package pl.cezaryregec.crypt.rsa;

import pl.cezaryregec.exception.APIException;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

public class RsaVerifier {

    private final PublicKey publicKey;

    public RsaVerifier() throws GeneralSecurityException {
        RsaPublicKeyFactory rsaPublicKeyFactory = new RsaPublicKeyFactory();
        this.publicKey = rsaPublicKeyFactory.create();
    }

    public boolean verify(String input, String signature) throws GeneralSecurityException {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return verify(bytes, signatureBytes);
    }

    public boolean verify(byte[] input, byte[] inputSignature) throws GeneralSecurityException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(input);

        return signature.verify(inputSignature);
    }
}
