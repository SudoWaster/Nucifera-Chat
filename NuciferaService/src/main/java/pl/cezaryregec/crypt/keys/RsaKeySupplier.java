package pl.cezaryregec.crypt.keys;

import com.google.inject.Inject;
import org.apache.logging.log4j.Level;
import pl.cezaryregec.exception.APIException;
import pl.cezaryregec.logger.ApplicationLogger;
import pl.cezaryregec.logger.SecurityLogger;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RsaKeySupplier implements Supplier<Optional<KeyPair>> {

    private static final String PRIVATE_FILENAME = "rsa_private.pem";

    private final ApplicationLogger applicationLogger;
    private final SecurityLogger securityLogger;

    @Inject
    RsaKeySupplier(ApplicationLogger applicationLogger, SecurityLogger securityLogger) {
        this.applicationLogger = applicationLogger;
        this.securityLogger = securityLogger;
    }

    /**
     * Returns a key pair from classpath or {@link Optional#empty() empty Optional} if key cannot be read
     * PKCS#8 private key filename: {@value #PRIVATE_FILENAME}
     * Public key will be generated from private
     *
     * @return an {@link Optional} of a saved key pair
     */
    @Override
    public Optional<KeyPair> get() {
        try {
            return Optional.of(createFromResource());
        } catch (Exception ex) {
            applicationLogger.log(ex);
            securityLogger.log(ex);
        }
        return Optional.empty();
    }

    /**
     * Creates a key pair from resource
     * Reads a private key and generated public
     *
     * @return a {@link KeyPair} of keys
     * @throws Exception when cannot read or parse keys
     *                   (or a rare situation happens when KeyFactory Instance cannot be created)
     */
    private KeyPair createFromResource() throws Exception {
        PrivateKey privateKey = createPrivateKeyFromResource();
        PublicKey publicKey = createPublicKeyFromPrivate(privateKey);

        return new KeyPair(publicKey, privateKey);
    }

    /**
     * Creates a private key from resource of name {@value PRIVATE_FILENAME}
     *
     * @return a {@link PrivateKey}
     * @throws NoSuchAlgorithmException when there is no RSA algorithm present in the system
     * @throws InvalidKeySpecException  when key parsing went wrong
     */
    private PrivateKey createPrivateKeyFromResource() throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = readFromResource(PRIVATE_FILENAME);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * Creates a PKCS#8 encoded key spec from a given file
     *
     * @param filename a PKCS#8 key filename
     * @return a {@link PKCS8EncodedKeySpec} of read key
     */
    private PKCS8EncodedKeySpec readFromResource(String filename) {
        InputStream inputStream = getClass().getResourceAsStream(filename);
        InputStreamReader keyReader = new InputStreamReader(inputStream);
        String key = new BufferedReader(keyReader)
                .lines()
                .collect(Collectors.joining("\n"))

                .replace("-----BEGIN PRIVATE KEY-----\n", "")
                .replace("\n-----END PRIVATE KEY-----\n", "")

                .replace("-----BEGIN PUBLIC KEY-----\n", "")
                .replace("\n-----END PUBLIC KEY-----\n", "");

        return new PKCS8EncodedKeySpec(DatatypeConverter.parseBase64Binary(key));
    }

    /**
     * Creates public key from private
     *
     * @param privateKey a {@link PrivateKey} to generate public from
     * @return a corresponding {@link PublicKey}
     * @throws NoSuchAlgorithmException when there is no RSA algorithm present in the system
     * @throws InvalidKeySpecException  when key parsing went wrong
     */
    private PublicKey createPublicKeyFromPrivate(PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPrivateCrtKey privateCert = (RSAPrivateCrtKey) privateKey;

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(privateCert.getModulus(), privateCert.getPublicExponent());

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(publicKeySpec);
    }
}
