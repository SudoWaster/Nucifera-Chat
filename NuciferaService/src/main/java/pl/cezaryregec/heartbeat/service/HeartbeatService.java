package pl.cezaryregec.heartbeat.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import pl.cezaryregec.auth.session.IdentityService;
import pl.cezaryregec.user.models.User;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.Optional;

public class HeartbeatService {
    private static final long THREE_MINUTES_IN_MS = 180000;

    private final IdentityService identityService;
    private final Provider<EntityManager> entityManagerProvider;

    @Inject
    public HeartbeatService(
            IdentityService identityService,
            Provider<EntityManager> entityManagerProvider
    ) {
        this.identityService = identityService;
        this.entityManagerProvider = entityManagerProvider;
    }

    /**
     * Saves 'active' flag for user
     */
    public void rememberLiveClient() {
        Optional<User> boundUser = identityService.getBoundUser();
        if (boundUser.isPresent()) {
            User user = boundUser.get();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis() + THREE_MINUTES_IN_MS);
            user.setLastSeen(timestamp);

            entityManagerProvider.get().merge(user);
            identityService.bindUser(user);
        }
    }
}
