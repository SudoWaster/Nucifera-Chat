package pl.cezaryregec.user;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import pl.cezaryregec.config.ConfigSupplier;
import pl.cezaryregec.crypt.CredentialsCombiner;
import pl.cezaryregec.crypt.HashGenerator;
import pl.cezaryregec.exception.APIException;
import pl.cezaryregec.user.models.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Optional;

public class UserService {

    private final Provider<EntityManager> entityManagerProvider;
    private final ConfigSupplier configSupplier;
    private final CredentialsCombiner credentialsCombiner;
    private final HashGenerator hashGenerator;

    @Inject
    public UserService(
            Provider<EntityManager> entityManagerProvider,
            ConfigSupplier configSupplier,
            CredentialsCombiner credentialsCombiner,
            HashGenerator hashGenerator
    ) {
        this.entityManagerProvider = entityManagerProvider;
        this.configSupplier = configSupplier;
        this.credentialsCombiner = credentialsCombiner;
        this.hashGenerator = hashGenerator;
    }

    @Transactional
    public Optional<User> loginAndGet(String username, String password) throws APIException {
        Optional<User> user = getUser(username);

        if (user.isPresent()) {
            String salt = configSupplier.get().getSecurity().getSalt();
            String encodedPassword = credentialsCombiner.combine(username, password, salt);
            String hashedPassword = hashGenerator.encode(encodedPassword);
            if (hashedPassword.equals(user.get().getPassword())) {
                return user;
            }
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<User> getUser(String username) {
        TypedQuery<User> query = entityManagerProvider.get().createNamedQuery("User.findByUsername", User.class);
        query.setParameter("username", username);

        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}
