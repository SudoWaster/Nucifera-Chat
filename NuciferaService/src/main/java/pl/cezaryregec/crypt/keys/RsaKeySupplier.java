package pl.cezaryregec.crypt.keys;

import com.google.inject.Inject;
import org.apache.logging.log4j.Level;
import pl.cezaryregec.logger.ApplicationLogger;
import pl.cezaryregec.logger.SecurityLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.*;
import java.util.Optional;
import java.util.function.Supplier;

public class RsaKeySupplier implements Supplier<Optional<KeyPair>> {

    private static final int KEYSIZE = 2048;
    private static final String PUBLIC_FILENAME = "public.rsa";
    private static final String PRIVATE_FILENAME = "private.rsa";

    private final RsaKeyPairGeneratorFactory keyPairGeneratorFactory;
    private final ApplicationLogger applicationLogger;
    private final SecurityLogger securityLogger;

    @Inject
    RsaKeySupplier(RsaKeyPairGeneratorFactory keyPairGeneratorFactory, ApplicationLogger applicationLogger, SecurityLogger securityLogger) {
        this.keyPairGeneratorFactory = keyPairGeneratorFactory;
        this.applicationLogger = applicationLogger;
        this.securityLogger = securityLogger;
    }

    @Override
    public Optional<KeyPair> get() {
        Optional<KeyPair> result;
        try {
            result = Optional.of(getFromResource());
        } catch (Exception ex) {
            applicationLogger.log(ex);
            securityLogger.log(ex);
            result = Optional.ofNullable(tryGeneratingKeyPair());
        }
        return result;
    }

    private KeyPair getFromResource() throws IOException, ClassNotFoundException {
        InputStream publicStream = getClass().getResourceAsStream(PUBLIC_FILENAME);
        InputStream privateStream = getClass().getResourceAsStream(PRIVATE_FILENAME);
        ObjectInputStream publicKeyParser = new ObjectInputStream(publicStream);
        ObjectInputStream privateKeyParser = new ObjectInputStream(privateStream);
        PublicKey publicKey = (PublicKey) publicKeyParser.readObject();
        PrivateKey privateKey = (PrivateKey) privateKeyParser.readObject();

        return new KeyPair(publicKey, privateKey);
    }

    private KeyPair tryGeneratingKeyPair() {
        try {
            KeyPair pair = getGeneratedKeyPair();
            return pair;
        } catch (NoSuchAlgorithmException ex) {
            securityLogger.log(ex);
            securityLogger.log("Cannot find encryption algorithm.", Level.FATAL);
            return null;
        }
    }

    private KeyPair getGeneratedKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = keyPairGeneratorFactory.create();
        generator.initialize(KEYSIZE, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        return pair;
    }

    private void saveKeyPair(KeyPair pair) {
        PublicKey publicKey = pair.getPublic();
        PrivateKey privateKey = pair.getPrivate();
        // TODO
    }
}
