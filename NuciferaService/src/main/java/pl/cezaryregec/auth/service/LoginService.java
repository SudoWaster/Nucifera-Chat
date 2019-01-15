package pl.cezaryregec.auth.service;

import com.google.inject.Inject;
import pl.cezaryregec.auth.AuthState;
import pl.cezaryregec.auth.models.LoginQuery;
import pl.cezaryregec.auth.models.PostAuthQuery;
import pl.cezaryregec.auth.models.PostAuthResponse;
import pl.cezaryregec.auth.session.IdentityService;
import pl.cezaryregec.exception.APIException;
import pl.cezaryregec.user.UserService;
import pl.cezaryregec.user.models.User;

import javax.ws.rs.ForbiddenException;
import java.util.Optional;

public class LoginService {
    private final IdentityService identityService;
    private final UserService userService;

    @Inject
    public LoginService(IdentityService identityService, UserService userService) {
        this.identityService = identityService;
        this.userService = userService;
    }

    public void login(LoginQuery loginQuery) throws APIException {
        String username = loginQuery.getUsername();
        String password = loginQuery.getPassword();
        Optional<User> user = userService.loginAndGet(username, password);

        if (identityService.isTokenValid() && user.isPresent()) {
            identityService.bindUser(user.get());
        } else {
            throw new ForbiddenException();
        }
    }

    public void signOut() {
        identityService.invalidate();
    }
}
