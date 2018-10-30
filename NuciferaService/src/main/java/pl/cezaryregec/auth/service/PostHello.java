package pl.cezaryregec.auth.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import pl.cezaryregec.auth.AuthState;
import pl.cezaryregec.auth.models.AuthToken;
import pl.cezaryregec.auth.session.Identity;
import pl.cezaryregec.crypt.HashGenerator;
import pl.cezaryregec.exception.APIException;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.sql.Timestamp;

public class PostHello implements PostAuth {
    private final HashGenerator hashGenerator;
    private final Provider<EntityManager> entityManagerProvider;
    private final Identity identity;

    @Inject
    PostHello(HashGenerator hashGenerator, Provider<EntityManager> entityManagerProvider, Identity identity) {
        this.hashGenerator = hashGenerator;
        this.entityManagerProvider = entityManagerProvider;
        this.identity = identity;
    }

    @Override
    public AuthToken execute(PostAuthQuery postAuthQuery) throws APIException {
        AuthToken authToken = new AuthToken();

        BigInteger challenge = postAuthQuery.getChallenge();
        Long currentTime = System.currentTimeMillis();

        authToken.setToken(hashGenerator.encode(challenge.toString() + currentTime));
        authToken.setAuthState(AuthState.HELLO);
        authToken.setExpiration(new Timestamp(currentTime));
        authToken.setChallenge(postAuthQuery.getChallenge());
        authToken.setFingerprint(identity.getFingerprint());

        entityManagerProvider.get().merge(authToken);
        identity.setToken(authToken);
        identity.setCipherSpec(true);

        return authToken;
    }
}
