package pl.cezaryregec.auth;

import com.google.inject.Inject;
import pl.cezaryregec.auth.service.PostAuth;
import pl.cezaryregec.auth.service.PostHello;

public class AuthResponseFactory {
    private final PostHello postHello;

    @Inject
    public AuthResponseFactory(PostHello postHello) {
        this.postHello = postHello;
    }

    public PostAuth create(ClientAuthState state) {
        if (state == null) {
            throw new IllegalStateException("State is null or illegal");
        }

        switch (state) {
            case HELLO_INIT:
                return postHello;
        }

        throw new IllegalStateException(state + " is not a valid auth state");
    }
}
