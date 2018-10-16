package pl.cezaryregec.filter;

import com.google.inject.Inject;
import pl.cezaryregec.auth.session.Identity;
import pl.cezaryregec.auth.session.IdentityValidator;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class RequestCommunicationDecryptorFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest request;

    private final com.google.inject.Provider<Identity> identityProvider;
    private final IdentityValidator identityValidator;

    @Inject
    public RequestCommunicationDecryptorFilter(com.google.inject.Provider<Identity> identityProvider, IdentityValidator identityValidator) {
        this.identityProvider = identityProvider;
        this.identityValidator = identityValidator;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String fingerprint = getFingerprint();
        identityProvider.get().setFingerprint(fingerprint);

        if (identityValidator.isTokenValid()) {
            identityValidator.renewToken();
            // TODO: symmetric decryption
        } else {
            // TODO: asymmetric decryption
        }
    }

    /**
     * Creates client fingerprint
     *
     * @return String of distinct user client data
     */
    private String getFingerprint() {
        return request.getRemoteAddr() + request.getRemoteHost() + request.getHeader("User-Agent");
    }
}
