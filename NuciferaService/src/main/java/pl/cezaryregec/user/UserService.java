package pl.cezaryregec.user;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import pl.cezaryregec.config.ConfigSupplier;
import pl.cezaryregec.crypt.CredentialsCombiner;
import pl.cezaryregec.crypt.HashGenerator;
import pl.cezaryregec.crypt.PasswordEncoder;
import pl.cezaryregec.exception.APIException;
import pl.cezaryregec.user.models.ChangePasswordRequest;
import pl.cezaryregec.user.models.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.ForbiddenException;
import java.util.Optional;

@Transactional
public class UserService {

    private final Provider<EntityManager> entityManagerProvider;
    private final PasswordEncoder passwordEncoder;

    @Inject
    public UserService(
            Provider<EntityManager> entityManagerProvider,
            PasswordEncoder passwordEncoder) {
        this.entityManagerProvider = entityManagerProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> loginAndGet(String username, String password) throws APIException {
        Optional<User> user = getUser(username);

        if (user.isPresent()) {
            String hashedPassword = passwordEncoder.encode(username, password);
            if (hashedPassword.equals(user.get().getPassword())) {
                return user;
            }
        }

        return Optional.empty();
    }

    private Optional<User> getUser(String username) {
        TypedQuery<User> query = entityManagerProvider.get().createNamedQuery("User.findByUsername", User.class);
        query.setParameter("username", username);

        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public void create(User user) throws APIException {
        if (getUser(user.getUsername()).isPresent()) {
            throw new ForbiddenException("Username is already taken");
        }

        String username = user.getUsername();
        String plainPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(username, plainPassword);
        user.setPassword(encodedPassword);

        entityManagerProvider.get().persist(user);
    }

    public void changePassword(ChangePasswordRequest request) throws APIException {
        Optional<User> existingUser = getUser(request.getUsername());
        if (!existingUser.isPresent()) {
            throw new ForbiddenException("User does not exist");
        }

        String oldCombinedPassword = passwordEncoder.encode(request.getUsername(), request.getPassword());
        if (!oldCombinedPassword.equals(existingUser.get().getPassword())) {
            throw new ForbiddenException("Wrong credentials");
        }

        String newCombinedPassword = passwordEncoder.encode(request.getUsername(), request.getPassword());
        User user = existingUser.get();
        user.setPassword(newCombinedPassword);

        entityManagerProvider.get().persist(user);
    }
}
