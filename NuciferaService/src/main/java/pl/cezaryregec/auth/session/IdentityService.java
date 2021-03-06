package pl.cezaryregec.auth.session;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.google.inject.servlet.RequestScoped;
import pl.cezaryregec.auth.models.AuthToken;
import pl.cezaryregec.config.ConfigSupplier;
import pl.cezaryregec.crypt.HashGenerator;
import pl.cezaryregec.exception.APIException;
import pl.cezaryregec.user.models.User;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import javax.ws.rs.NotAuthorizedException;
import java.sql.Timestamp;
import java.time.Clock;
import java.util.Optional;

@RequestScoped
public class IdentityService {
    private final Provider<EntityManager> entityManagerProvider;
    private final HashGenerator hashGenerator;
    private final ConfigSupplier configSupplier;
    private final Clock clock;

    private Identity identity = new Identity();

    @Inject
    public IdentityService(
            Provider<EntityManager> entityManagerProvider,
            HashGenerator hashGenerator,
            ConfigSupplier configSupplier,
            Clock clock
    ) {
        this.entityManagerProvider = entityManagerProvider;
        this.hashGenerator = hashGenerator;
        this.configSupplier = configSupplier;
        this.clock = clock;
    }

    /**
     * Checks if current token session is valid - if a security exists, has not expired and has a valid fingerprint
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

        Long expirationFromConfig = configSupplier.get().getSecurity().getTokenExpiration();
        Timestamp currentTimestamp = new Timestamp(clock.millis());
        Timestamp expirationTime = new Timestamp(identity.getToken().get().getExpiration().getTime() + expirationFromConfig);
        return expirationTime.before(currentTimestamp);
    }

    /**
     * Renews token to keep it valid or delete it if expired
     */
    @Transactional
    public void renewToken() {
        if (!isTokenValid()) {
            if (hasExpired()) {
                invalidate();
            }
            // no use renewing expired or non existent token
            return;
        }
        Timestamp currentTime = new Timestamp(clock.millis());
        identity.getToken().get().setExpiration(currentTime);
        entityManagerProvider.get().merge(identity.getToken().get());
    }

    /**
     * Caches request token (with current fingerprint)
     *
     * @param token {@link AuthToken} containing authentication data
     */
    @Transactional
    public void setToken(@NotNull AuthToken token) {
        invalidate();

        AuthToken managedToken = entityManagerProvider.get().find(AuthToken.class, token.getToken());
        if (managedToken != null) {
            entityManagerProvider.get().merge(token);
        } else {
            entityManagerProvider.get()
                                 .persist(token);
        }
        identity.setToken(Optional.of(token));
    }

    /**
     * Invalidates token
     */
    @Transactional
    public void invalidate() {
        if (identity.getToken().isPresent()) {
            // make sure we have a managed entity
            AuthToken token = entityManagerProvider.get().find(AuthToken.class, identity.getToken().get().getToken());

            if (token != null) {
                entityManagerProvider.get()
                                     .remove(token);
            }
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
    @Transactional
    public void setCipherSpec(Boolean enabled) {
        if (identity.getToken().isPresent()) {
            AuthToken token = identity.getToken().get();
            token.setCipherSpec(enabled);
            entityManagerProvider.get().merge(token);
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
     * @param challenge {@String} challenge used in encryption
     * @throws APIException when cannot encode a token id
     */
    public void createToken(String challenge) throws APIException {
        AuthToken token = new AuthToken();
        Long currentTime = clock.millis();
        String tokenId = hashGenerator.encode(currentTime + identity.getFingerprint());
        token.setToken(tokenId);
        token.setExpiration(new Timestamp(System.currentTimeMillis()));
        token.setChallenge(challenge);
        token.setCipherSpec(false);
        token.setFingerprint(identity.getFingerprint());
        setToken(token);
    }

    /**
     * Returns current token id
     *
     * @return {@link String} current request token
     */
    @Transactional
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
    @Transactional
    public void retrieveToken(String id) {
        AuthToken token = entityManagerProvider.get().find(AuthToken.class, id);
        if (token != null) {
            setToken(token);
        } else {
            identity.setToken(Optional.empty());
        }
    }


    /**
     * Bind logged user to token
     *
     * @param user logged user
     */
    @Transactional
    public void bindUser(User user) {
        if (!identity.getToken().isPresent()) {
            throw new NotAuthorizedException("Token is invalid");
        }
        AuthToken token = identity.getToken().get();
        token.setUser(user);
        entityManagerProvider.get().merge(token);
    }

    /**
     * Returns bound (logged) user
     *
     * @return user
     */
    public Optional<User> getBoundUser() {
        if (identity.getToken().isPresent()) {
            AuthToken token = identity.getToken().get();
            return Optional.ofNullable(token.getUser());
        }
        return Optional.empty();
    }
}

