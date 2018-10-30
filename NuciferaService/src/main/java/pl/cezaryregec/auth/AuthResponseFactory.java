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
    private final ApplicationLogger applicationLogger;

    @Inject
    public AuthResponseFactory(PostHello postHello, HelloError helloError, Identity identity, ApplicationLogger applicationLogger) {
        this.postHello = postHello;
        this.helloError = helloError;
        this.identity = identity;
        this.applicationLogger = applicationLogger;
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
        applicationLogger.log(e);
        identity.invalidate();
        return helloError;
    }
}
