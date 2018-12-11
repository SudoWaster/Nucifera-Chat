package pl.cezaryregec.user;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import pl.cezaryregec.user.models.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;

public class UserService {

    private final Provider<EntityManager> entityManagerProvider;

    @Inject
    public UserService(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @Transactional
    public User getUser(String username) {
        TypedQuery<User> query = entityManagerProvider.get().createNamedQuery("User.findByUsername", User.class);
        query.setParameter("username", username);

        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            throw new NotFoundException(ex);
        }
    }
}
