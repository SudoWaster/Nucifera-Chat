package pl.cezaryregec.cucumber.steps;

import com.google.inject.Inject;
import com.google.inject.Provider;
import cucumber.api.java.Before;
import org.mockito.Mockito;
import pl.cezaryregec.auth.session.IdentityService;
import pl.cezaryregec.config.ConfigSupplier;
import pl.cezaryregec.crypt.HashGenerator;
import pl.cezaryregec.cucumber.steps.utils.Tokens;
import pl.cezaryregec.logger.ApplicationLogger;

import javax.persistence.EntityManager;
import java.time.Clock;

public class TokenHooks {
    private final Tokens tokens;

    @Inject
    public TokenHooks(Tokens tokens) {
        this.tokens = tokens;
    }

    @Before("@TokenUsed")
    public void initToken() {
        ApplicationLogger applicationLogger = Mockito.mock(ApplicationLogger.class);
        ConfigSupplier configSupplier = new ConfigSupplier(applicationLogger);
        HashGenerator hashGenerator = Mockito.mock(HashGenerator.class);

        EntityManager entityManager = Mockito.mock(EntityManager.class);
        Mockito.when(entityManager.find(Mockito.any(), Mockito.any()))
                .thenReturn(tokens.token);
        Provider<EntityManager> entityManagerProvider = Mockito.mock(Provider.class);
        Mockito.when(entityManagerProvider.get())
                .thenReturn(entityManager);

        tokens.clock = Mockito.mock(Clock.class);

        tokens.identityService = new IdentityService(entityManagerProvider, hashGenerator, configSupplier, tokens.clock);
    }
}
