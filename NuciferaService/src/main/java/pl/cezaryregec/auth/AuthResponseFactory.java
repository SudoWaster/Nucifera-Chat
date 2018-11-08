package pl.cezaryregec.auth;

import com.google.inject.Inject;
import pl.cezaryregec.auth.service.PostHandshake;
import pl.cezaryregec.auth.session.IdentityService;
import pl.cezaryregec.logger.ApplicationLogger;
import pl.cezaryregec.auth.service.HelloError;
import pl.cezaryregec.auth.service.PostAuth;
import pl.cezaryregec.auth.service.PostHello;

import javax.validation.constraints.NotNull;

public class AuthResponseFactory {

    private final IdentityService identityService;
    private final ApplicationLogger applicationLogger;

    private final PostHello postHello;
    private final PostHandshake postHandshake;
    private final HelloError helloError;

    @Inject
    public AuthResponseFactory(PostHello postHello, HelloError helloError, IdentityService identityService, ApplicationLogger applicationLogger, PostHandshake postHandshake) {
        this.postHello = postHello;
        this.helloError = helloError;
        this.identityService = identityService;
        this.applicationLogger = applicationLogger;
        this.postHandshake = postHandshake;
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
            case HELLO_CLIENT_DONE:
                return postHandshake;
            case HELLO_CLIENT_REFUSED:
                return helloError;

        }

        throw new IllegalStateException(state + " is not a valid ClientAuthState");
    }

    private PostAuth handleError(Throwable e) {
        // an unexpected error has occured
        // eg. wrong keys used
        applicationLogger.log(e);
        return helloError;
    }
}
