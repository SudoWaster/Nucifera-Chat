package pl.cezaryregec.auth.session;

import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import java.sql.Timestamp;

public class IdentityValidator {
    private final Identity identity;
    private final Provider<EntityManager> entityManagerProvider;

    @Inject
    IdentityValidator(Identity identity, Provider<EntityManager> entityManagerProvider) {
        this.identity = identity;
        this.entityManagerProvider = entityManagerProvider;
    }

    /**
     * Checks if current token session is valid
     *
     * @return true if valid
     */
    public boolean isTokenValid() {
        if (identity.getToken() == null) {
            return false;
        }

        Timestamp expirationTime = new Timestamp(System.currentTimeMillis());
        boolean hasExpired = identity.getToken().getExpiration().before(expirationTime);
        boolean fingerprintValid = identity.getFingerprint().equals(identity.getToken().getFingerprint());
        return fingerprintValid && !hasExpired;
    }


    /**
     * Renews token to keep it valid
     */
    public void renewToken() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        identity.getToken().setExpiration(currentTime);
        entityManagerProvider.get().merge(identity.getToken());
    }
}
