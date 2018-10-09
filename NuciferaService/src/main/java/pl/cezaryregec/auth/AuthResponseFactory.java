package pl.cezaryregec.auth;

import com.google.inject.Inject;
import net.bytebuddy.implementation.bytecode.Throw;
import pl.cezaryregec.ApplicationLogger;
import pl.cezaryregec.auth.service.HelloError;
import pl.cezaryregec.auth.service.PostAuth;
import pl.cezaryregec.auth.service.PostHello;

import javax.validation.constraints.NotNull;

public class AuthResponseFactory {
    private final PostHello postHello;

    private final HelloError helloError;

    @Inject
    public AuthResponseFactory(PostHello postHello, HelloError helloError) {
        this.postHello = postHello;
        this.helloError = helloError;
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
        return helloError;
    }
}
