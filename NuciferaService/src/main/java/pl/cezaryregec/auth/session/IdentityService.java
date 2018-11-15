package pl.cezaryregec.auth.session;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestScoped;
import pl.cezaryregec.auth.models.AuthToken;
import pl.cezaryregec.config.ConfigSupplier;
import pl.cezaryregec.crypt.HashGenerator;
import pl.cezaryregec.exception.APIException;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Optional;

@RequestScoped
public class IdentityService {
    private final Provider<EntityManager> entityManagerProvider;
    private final HashGenerator hashGenerator;
    private final ConfigSupplier configSupplier;

    private Identity identity;

    @Inject
    IdentityService(Provider<EntityManager> entityManagerProvider, HashGenerator hashGenerator, ConfigSupplier configSupplier) {
        this.entityManagerProvider = entityManagerProvider;
        this.hashGenerator = hashGenerator;
        this.configSupplier = configSupplier;
        this.identity = new Identity();
    }

    /**
     * Checks if current token session is valid - if a token exists, has not expired and has a valid fingerprint
     *
     * @return true if valid
     */
    public boolean isTokenValid() {
        if (!identity.getToken().isPresent()) {
            return false;
        }

        boolean fingerprintValid = identity.getFingerprint().equals(identity.getToken().get().getFingerprint());
        return fingerprintValid && !hasExpired();
    }

    /**
     * Checks if token has expired
     *
     * @return true if it has
     */
    public boolean hasExpired() {
        if (!identity.getToken().isPresent()) {
            return true;
        }

        Long expirationFromConfig = configSupplier.get().getToken().getExpiration();
        Timestamp expirationTime = new Timestamp(System.currentTimeMillis() + expirationFromConfig);
        return identity.getToken().get().getExpiration().before(expirationTime);
    }

    /**
     * Renews token to keep it valid or delete it if expired
     */
    public void renewToken() {
        if (!isTokenValid()) {
            if (hasExpired()) {
                invalidate();
            }
            // no use renewing expired or non existent token
            return;
        }
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        identity.getToken().get().setExpiration(currentTime);
        entityManagerProvider.get().merge(identity.getToken().get());
    }

    /**
     * Caches request token (with current fingerprint)
     *
     * @param token {@link AuthToken} containing authentication data
     */
    public void setToken(AuthToken token) {
        invalidate();
        token.setFingerprint(identity.getFingerprint());
        identity.setToken(Optional.of(token));
        entityManagerProvider.get().merge(token);
    }

    /**
     * Invalidates token
     */
    public void invalidate() {
        if (identity.getToken().isPresent()) {
            entityManagerProvider.get().remove(identity.getToken());
        }
        identity.setToken(Optional.empty());
    }

    /**
     * Caches request fingerprint
     *
     * @param fingerprint (almost) unique client {@link String}
     */
    public void setFingerprint(String fingerprint) {
        identity.setFingerprint(fingerprint);
    }

    /**
     * Sets cipher spec
     *
     * @param enabled {@code true} if symmetrical encryption enabled
     */
    public void setCipherSpec(Boolean enabled) {
        if (identity.getToken().isPresent()) {
            AuthToken token = identity.getToken().get();
            token.setCipherSpec(enabled);
            setToken(token);
        }
    }

    /**
     * Checks if session uses cipher spec
     *
     * @return true if session is valid and uses cipher spec
     */
    public boolean hasCipherSpec() {
        return isTokenValid() && identity.getToken().get().getCipherSpec();
    }

    /**
     * Returns authentication challenge for symmetrical encryption
     *
     * @return {@link String} challenge
     * @throws APIException when challenge is not present
     */
    public String getChallenge() throws APIException {
        if (identity.getToken().isPresent()) {
            return identity.getToken().get().getChallenge();
        }
        throw new APIException("Secure connection hasn't been established or recognized.", 400);
    }

    /**
     * Creates new session token with given challenge
     *
     * @param {@String} challenge used in encryption
     * @throws APIException when cannot encode a token id
     */
    public void createToken(String challenge) throws APIException {
        AuthToken token = new AuthToken();
        Long currentTime = System.currentTimeMillis();
        String tokenId = hashGenerator.encode(currentTime + identity.getFingerprint());
        token.setToken(tokenId);
        token.setExpiration(new Timestamp(System.currentTimeMillis()));
        token.setChallenge(challenge);
        token.setCipherSpec(false);
        setToken(token);
    }

    /**
     * Returns current token id
     *
     * @return {@link String} current request token
     */
    public String getTokenId() {
        if (identity.getToken().isPresent()) {
            return identity.getToken().get().getToken();
        }
        return "";
    }

    /**
     * Retrieves token from database
     *
     * @param id request token
     */
    public void retrieveToken(String id) {
        AuthToken token = entityManagerProvider.get().find(AuthToken.class, id);
        setToken(token);
    }
}
