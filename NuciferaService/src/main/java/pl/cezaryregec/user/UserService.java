package pl.cezaryregec.user;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import pl.cezaryregec.user.models.User;

import javax.persistence.EntityManager;

public class UserService {

    private final Provider<EntityManager> entityManagerProvider;

    @Inject
    public UserService(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @Transactional
    public User getUser(String username) {
        return entityManagerProvider.get().find(User.class, username);
    }
}
