package pl.cezaryregec.cucumber.steps;

import com.google.inject.Inject;
import com.google.inject.Provider;
import cucumber.api.java.Before;
import org.mockito.Mockito;
import pl.cezaryregec.config.ConfigSupplier;
import pl.cezaryregec.crypt.CredentialsCombiner;
import pl.cezaryregec.crypt.HashGenerator;
import pl.cezaryregec.crypt.Sha256Generator;
import pl.cezaryregec.cucumber.steps.utils.Users;
import pl.cezaryregec.logger.ApplicationLogger;
import pl.cezaryregec.user.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class UserHooks {
    private final Users users;

    @Inject
    public UserHooks(Users users) {
        this.users = users;
    }

    @Before("@UserUsed")
    public void initUser() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        Mockito.when(entityManager.find(Mockito.any(), Mockito.any()))
                .thenReturn(users.user);

        TypedQuery<Object> query = Mockito.mock(TypedQuery.class);
        Mockito.when(query.getSingleResult())
                .thenReturn(users.user);
        Mockito.when(entityManager.createNamedQuery(Mockito.any(), Mockito.any()))
                .thenReturn(query);

        Provider<EntityManager> entityManagerProvider = Mockito.mock(Provider.class);
        Mockito.when(entityManagerProvider.get())
                .thenReturn(entityManager);


        ApplicationLogger applicationLogger = Mockito.mock(ApplicationLogger.class);
        ConfigSupplier configSupplier = new ConfigSupplier(applicationLogger);

        CredentialsCombiner credentialsCombiner = new CredentialsCombiner();

        HashGenerator hashGenerator = new Sha256Generator();

        users.userService = new UserService(entityManagerProvider, configSupplier, credentialsCombiner, hashGenerator);
    }
}
