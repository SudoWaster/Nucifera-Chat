package pl.cezaryregec.filter;

import com.google.inject.Provider;
import pl.cezaryregec.auth.session.IdentityService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import java.io.IOException;

/**
 * Additional filter to retrieve token when request body is empty
 * <p>
 * Empty request body does not trigger RequestEncryptedReaderInterceptor which
 * triggers token retrieval
 */
public class PlainRequestTokenReceiverFilter implements ContainerRequestFilter {

    @Context
    HttpServletRequest request;

    private final Provider<IdentityService> identityServiceProvider;

    @Inject
    public PlainRequestTokenReceiverFilter(Provider<IdentityService> identityServiceProvider) {
        this.identityServiceProvider = identityServiceProvider;
    }

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        if (!identityServiceProvider.get().isTokenValid()) {
            String fingerprint = FingerprintFactory.create(request);
            identityServiceProvider.get().setFingerprint(fingerprint);

            // get token if it exists
            String tokenId = context.getHeaders().getFirst("X-Nucifera-Token");
            if (tokenId != null) {
                identityServiceProvider.get().retrieveToken(tokenId);
            }

            // check for validity and renew
            if (identityServiceProvider.get().isTokenValid()) {
                identityServiceProvider.get().renewToken();
            }
        }
    }
}
