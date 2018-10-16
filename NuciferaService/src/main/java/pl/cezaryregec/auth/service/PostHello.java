package pl.cezaryregec.auth.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import pl.cezaryregec.auth.AuthState;
import pl.cezaryregec.auth.models.AuthResponse;
import pl.cezaryregec.crypt.HashGenerator;

import javax.persistence.EntityManager;
import java.sql.Timestamp;

public class PostHello implements PostAuth {
    private final HashGenerator hashGenerator;
    private final Provider<EntityManager> entityManagerProvider;

    @Inject
    public PostHello(HashGenerator hashGenerator, Provider<EntityManager> entityManagerProvider) {
        this.hashGenerator = hashGenerator;
        this.entityManagerProvider = entityManagerProvider;
    }

    @Override
    public AuthResponse execute(PostAuthQuery postAuthQuery) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(hashGenerator.encode(postAuthQuery.getChallenge() + System.currentTimeMillis()));
        authResponse.setAuthState(AuthState.HELLO);
        authResponse.setExpiration(new Timestamp(System.currentTimeMillis()));
        entityManagerProvider.get().merge(authResponse);
        return authResponse;
    }
}
