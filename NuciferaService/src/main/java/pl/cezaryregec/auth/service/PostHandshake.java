package pl.cezaryregec.auth.service;

import com.google.inject.Inject;
import pl.cezaryregec.auth.AuthState;
import pl.cezaryregec.auth.models.PostAuthQuery;
import pl.cezaryregec.auth.models.PostAuthResponse;
import pl.cezaryregec.auth.session.IdentityService;
import pl.cezaryregec.exception.APIException;

public class PostHandshake implements PostAuth {
    private final IdentityService identityService;

    @Inject
    public PostHandshake(IdentityService identityService) {
        this.identityService = identityService;
    }

    @Override
    public PostAuthResponse execute(PostAuthQuery postAuthQuery) throws APIException {
        identityService.setCipherSpec(true);

        PostAuthResponse response = new PostAuthResponse();
        response.setState(AuthState.AUTH_VALID);
        return response;
    }
}