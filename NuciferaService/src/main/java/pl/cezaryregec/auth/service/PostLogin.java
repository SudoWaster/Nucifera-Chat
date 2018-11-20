package pl.cezaryregec.auth.service;

import com.google.inject.Inject;
import pl.cezaryregec.auth.AuthState;
import pl.cezaryregec.auth.models.PostAuthQuery;
import pl.cezaryregec.auth.models.PostAuthResponse;
import pl.cezaryregec.auth.session.IdentityService;
import pl.cezaryregec.exception.APIException;

public class PostLogin implements PostAuth {
    private final IdentityService identityService;

    @Inject
    public PostLogin(IdentityService identityService) {
        this.identityService = identityService;
    }

    @Override
    public PostAuthResponse execute(PostAuthQuery postAuthQuery) throws APIException {
        String username = postAuthQuery.getUsername();
        String password = postAuthQuery.getPassword();
        PostAuthResponse response = new PostAuthResponse();
        response.setState(AuthState.FAIL);

        if (identityService.isTokenValid() && identityService.loginUser(username, password)) {
            response.setState(AuthState.AUTH_VALID);
        }

        return response;
    }
}
