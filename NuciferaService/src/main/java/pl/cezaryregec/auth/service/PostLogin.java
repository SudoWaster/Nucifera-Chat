package pl.cezaryregec.auth.service;

import com.google.inject.Inject;
import pl.cezaryregec.auth.AuthState;
import pl.cezaryregec.auth.models.PostAuthQuery;
import pl.cezaryregec.auth.models.PostAuthResponse;
import pl.cezaryregec.auth.session.IdentityService;
import pl.cezaryregec.exception.APIException;
import pl.cezaryregec.user.UserService;
import pl.cezaryregec.user.models.User;

import java.util.Optional;

public class PostLogin implements PostAuth {
    private final IdentityService identityService;
    private final UserService userService;

    @Inject
    public PostLogin(IdentityService identityService, UserService userService) {
        this.identityService = identityService;
        this.userService = userService;
    }

    @Override
    public PostAuthResponse execute(PostAuthQuery postAuthQuery) throws APIException {
        String username = postAuthQuery.getUsername();
        String password = postAuthQuery.getPassword();
        PostAuthResponse response = new PostAuthResponse();
        response.setState(AuthState.LOGIN_FAIL);

        Optional<User> user = userService.loginAndGet(username, password);

        if (identityService.isTokenValid() && user.isPresent()) {
            identityService.bindUser(user.get());
            response.setState(AuthState.AUTH_VALID);
        }

        return response;
    }
}
