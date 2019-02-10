package pl.cezaryregec.user;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import pl.cezaryregec.auth.session.IdentityService;
import pl.cezaryregec.crypt.PasswordEncoder;
import pl.cezaryregec.exception.APIException;
import pl.cezaryregec.user.models.ChangePasswordRequest;
import pl.cezaryregec.user.models.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import java.util.Optional;

@Transactional
public class UserService {

    private final Provider<EntityManager> entityManagerProvider;
    private final PasswordEncoder passwordEncoder;
    private final IdentityService identityService;

    @Inject
    public UserService(
            Provider<EntityManager> entityManagerProvider,
            PasswordEncoder passwordEncoder, IdentityService identityService) {
        this.entityManagerProvider = entityManagerProvider;
        this.passwordEncoder = passwordEncoder;
        this.identityService = identityService;
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

    public void addContact(Long userId) {
        User boundUser = getBoundUser();
        if (boundUser.getId() == userId) {
            throw new ForbiddenException("Cannot add user you are logged as");
        }
        User user = getUser(userId);

        boundUser.getContacts().add(user);
        entityManagerProvider.get().merge(boundUser);
        user.getContacts().add(boundUser);
        entityManagerProvider.get().merge(user);
        identityService.bindUser(boundUser);
    }


    public void removeContact(Long userId) {
        User boundUser = getBoundUser();
        if (boundUser.getId() == userId) {
            throw new ForbiddenException("Cannot add user you are logged as");
        }
        User user = getUser(userId);

        boundUser.getContacts().remove(user);
        entityManagerProvider.get().merge(boundUser);
        user.getContacts().remove(boundUser);
        entityManagerProvider.get().merge(user);
        identityService.bindUser(boundUser);
    }

    private User getUser(Long userId) {
        User user = entityManagerProvider.get().find(User.class, userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user;
    }

    private User getBoundUser() {
        Optional<User> boundUser = identityService.getBoundUser();

        if (!boundUser.isPresent()) {
            throw new ForbiddenException("Not logged in");
        }

        return boundUser.get();
    }
}
