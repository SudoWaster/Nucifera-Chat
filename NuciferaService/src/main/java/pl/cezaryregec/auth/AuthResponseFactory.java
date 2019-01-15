package pl.cezaryregec.auth;

import com.google.inject.Inject;
import pl.cezaryregec.auth.service.*;

import javax.validation.constraints.NotNull;

public class AuthResponseFactory {

    private final PostHello postHello;
    private final PostHandshake postHandshake;
    private final HelloError helloError;

    @Inject
    public AuthResponseFactory(
            PostHello postHello,
            HelloError helloError,
            PostHandshake postHandshake
    ) {
        this.postHello = postHello;
        this.helloError = helloError;
        this.postHandshake = postHandshake;
    }

    /**
     * Creates encrypted session/token
     *
     * @param state current client-side state of a handshake
     * @return state handler
     */
    public PostAuth create(@NotNull ClientAuthState state) {
        switch (state) {
            case HELLO_INIT:
                return postHello;
            case HELLO_CLIENT_DONE:
                return postHandshake;
            case HELLO_CLIENT_REFUSED:
                return helloError;
        }

        throw new IllegalStateException(state + " is not a valid ClientAuthState");
    }
}
