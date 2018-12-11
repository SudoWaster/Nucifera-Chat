package pl.cezaryregec.auth;

import com.google.inject.Inject;
import pl.cezaryregec.auth.service.*;

import javax.validation.constraints.NotNull;

public class AuthResponseFactory {

    private final PostHello postHello;
    private final PostHandshake postHandshake;
    private final HelloError helloError;
    private final PostBye postBye;
    private final PostLogin postLogin;

    @Inject
    public AuthResponseFactory(
            PostHello postHello,
            HelloError helloError,
            PostHandshake postHandshake,
            PostBye postBye,
            PostLogin postLogin
    ) {
        this.postHello = postHello;
        this.helloError = helloError;
        this.postHandshake = postHandshake;
        this.postBye = postBye;
        this.postLogin = postLogin;
    }

    public PostAuth create(@NotNull ClientAuthState state) {
        switch (state) {
            case HELLO_INIT:
                return postHello;
            case HELLO_CLIENT_DONE:
                return postHandshake;
            case HELLO_CLIENT_REFUSED:
                return helloError;
            case LOGIN:
                return postLogin;
            case BYE:
                return postBye;
        }

        throw new IllegalStateException(state + " is not a valid ClientAuthState");
    }
}
