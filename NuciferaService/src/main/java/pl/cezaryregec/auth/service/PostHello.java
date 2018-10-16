package pl.cezaryregec.auth.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import pl.cezaryregec.auth.AuthState;
import pl.cezaryregec.auth.models.AuthToken;
import pl.cezaryregec.auth.session.Identity;
import pl.cezaryregec.crypt.HashGenerator;

import javax.persistence.EntityManager;
import java.sql.Timestamp;

public class PostHello implements PostAuth {
    private final HashGenerator hashGenerator;
    private final Provider<EntityManager> entityManagerProvider;
    private final Identity identity;

    @Inject
    public PostHello(HashGenerator hashGenerator, Provider<EntityManager> entityManagerProvider, Identity identity) {
        this.hashGenerator = hashGenerator;
        this.entityManagerProvider = entityManagerProvider;
        this.identity = identity;
    }

    @Override
    public AuthToken execute(PostAuthQuery postAuthQuery) {
        AuthToken authToken = new AuthToken();
        Long currentTime = System.currentTimeMillis();
        authToken.setToken(hashGenerator.encode(postAuthQuery.getChallenge() + currentTime));
        authToken.setAuthState(AuthState.HELLO);
        authToken.setExpiration(new Timestamp(currentTime));
        entityManagerProvider.get().merge(authToken);
        identity.setToken(authToken);
        return authToken;
    }
}
