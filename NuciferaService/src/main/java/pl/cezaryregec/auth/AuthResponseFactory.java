package pl.cezaryregec.auth;

import com.google.inject.Inject;
import pl.cezaryregec.ApplicationLogger;
import pl.cezaryregec.auth.service.HelloError;
import pl.cezaryregec.auth.service.PostAuth;
import pl.cezaryregec.auth.service.PostHello;
import pl.cezaryregec.auth.session.Identity;

import javax.validation.constraints.NotNull;

public class AuthResponseFactory {

    private final PostHello postHello;
    private final HelloError helloError;
    private final Identity identity;

    @Inject
    public AuthResponseFactory(PostHello postHello, HelloError helloError, Identity identity) {
        this.postHello = postHello;
        this.helloError = helloError;
        this.identity = identity;
    }

    public PostAuth create(ClientAuthState state) {
        try {
            return getProcessor(state);
        } catch (Exception e) {
            return handleError(e);
        }
    }

    private PostAuth getProcessor(@NotNull ClientAuthState state) {
        switch (state) {
            case HELLO_INIT:
                return postHello;
        }

        throw new IllegalStateException(state + " is not a valid ClientAuthState");
    }

    private PostAuth handleError(Throwable e) {
        // an unexpected error has occured
        // eg. wrong keys used
        ApplicationLogger.log(e);
        identity.setCipherSpec(false);
        identity.setToken(null);
        return helloError;
    }
}
