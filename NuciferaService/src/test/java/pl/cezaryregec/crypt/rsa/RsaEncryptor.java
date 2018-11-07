package pl.cezaryregec.crypt.rsa;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;

public class RsaEncryptor {

    private final PublicKey publicKey;

    public RsaEncryptor() throws GeneralSecurityException {
        RsaPublicKeyFactory rsaPublicKeyFactory = new RsaPublicKeyFactory();
        this.publicKey = rsaPublicKeyFactory.create();
    }

    public String encrypt(String input) throws GeneralSecurityException {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        byte[] result = encrypt(bytes);
        return Base64.getEncoder().encodeToString(result);
    }

    public byte[] encrypt(byte[] input) throws GeneralSecurityException {
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return decryptCipher.doFinal(input);
    }
}
