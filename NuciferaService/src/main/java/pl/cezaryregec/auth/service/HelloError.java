package pl.cezaryregec.auth.service;

import pl.cezaryregec.auth.AuthState;
import pl.cezaryregec.auth.models.AuthToken;

public class HelloError implements PostAuth {
    @Override
    public AuthToken execute(PostAuthQuery postAuthQuery) {
        AuthToken errorResponse = new AuthToken();
        errorResponse.setAuthState(AuthState.HELLO_FAIL);
        return errorResponse;
    }
}
