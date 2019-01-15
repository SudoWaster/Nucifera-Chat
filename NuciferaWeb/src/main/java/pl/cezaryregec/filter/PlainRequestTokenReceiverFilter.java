package pl.cezaryregec.filter;

import com.google.inject.Provider;
import pl.cezaryregec.auth.session.IdentityService;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

/**
 * Additional filter to retrieve token when request body is empty
 * <p>
 * Empty request body does not trigger RequestEncryptedReaderInterceptor which
 * triggers token retrieval
 */
public class PlainRequestTokenReceiverFilter implements ContainerRequestFilter {

    private final TokenInitializer tokenInitializer;

    @Inject
    public PlainRequestTokenReceiverFilter(TokenInitializer tokenInitializer) {
        this.tokenInitializer = tokenInitializer;
    }

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        String token = context.getHeaders().getFirst("X-Nucifera-Token");
        tokenInitializer.init(token);
    }
}
