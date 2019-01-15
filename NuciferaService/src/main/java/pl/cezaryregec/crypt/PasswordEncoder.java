package pl.cezaryregec.crypt;

import com.google.inject.Inject;
import pl.cezaryregec.config.ConfigSupplier;
import pl.cezaryregec.exception.APIException;

public class PasswordEncoder {
    private final ConfigSupplier configSupplier;
    private final CredentialsCombiner credentialsCombiner;
    private final HashGenerator hashGenerator;

    @Inject
    public PasswordEncoder(ConfigSupplier configSupplier, CredentialsCombiner credentialsCombiner, HashGenerator hashGenerator) {
        this.configSupplier = configSupplier;
        this.credentialsCombiner = credentialsCombiner;
        this.hashGenerator = hashGenerator;
    }

    public String encode(String username, String password) throws APIException {
        String salt = configSupplier.get().getSecurity().getSalt();
        String encodedPassword = credentialsCombiner.combine(username, password, salt);
        String hashedPassword = hashGenerator.encode(encodedPassword);
        return hashedPassword;
    }
}
