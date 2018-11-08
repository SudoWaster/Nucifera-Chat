package pl.cezaryregec.auth.service;

import com.google.inject.Inject;
import pl.cezaryregec.auth.AuthState;
import pl.cezaryregec.auth.models.PostAuthQuery;
import pl.cezaryregec.auth.models.PostAuthResponse;
import pl.cezaryregec.auth.session.IdentityService;

public class HelloError implements PostAuth {
    private final IdentityService identityService;

    @Inject
    public HelloError(IdentityService identityService) {
        this.identityService = identityService;
    }

    @Override
    public PostAuthResponse execute(PostAuthQuery postAuthQuery) {
        identityService.invalidate();
        PostAuthResponse postAuthResponse = new PostAuthResponse();
        postAuthResponse.setState(AuthState.HELLO_FAIL);
        return postAuthResponse;
    }
}
