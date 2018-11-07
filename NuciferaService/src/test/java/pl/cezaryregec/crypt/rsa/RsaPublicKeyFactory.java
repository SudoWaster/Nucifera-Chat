package pl.cezaryregec.crypt.rsa;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.stream.Collectors;

public class RsaPublicKeyFactory {

    /**
     * Creates public key from file
     * It is a corresponding
     *
     * @return
     * @throws GeneralSecurityException
     */
    public PublicKey create() throws GeneralSecurityException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("rsa_public.pem");
        InputStreamReader keyReader = new InputStreamReader(inputStream);
        String key = new BufferedReader(keyReader)
                .lines()
                .collect(Collectors.joining("\n"))

                .replace("-----BEGIN PUBLIC KEY-----\n", "")
                .replace("\n-----END PUBLIC KEY-----\n", "");

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(DatatypeConverter.parseBase64Binary(key));

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

}
