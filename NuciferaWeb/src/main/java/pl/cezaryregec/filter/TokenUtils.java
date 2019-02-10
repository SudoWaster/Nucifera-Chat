package pl.cezaryregec.filter;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provider;
import pl.cezaryregec.auth.session.IdentityService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.Context;

public class TokenUtils {

    static final ImmutableSet UNENCRYPTED_PATHS;

    static {
        UNENCRYPTED_PATHS = ImmutableSet.of("/auth");
    }

    private final Provider<IdentityService> identityServiceProvider;

    @Inject
    public TokenUtils(Provider<IdentityService> identityServiceProvider) {
        this.identityServiceProvider = identityServiceProvider;
    }

    void init(HttpServletRequest request, String token) {
        if (!identityServiceProvider.get().isTokenValid()) {
            String fingerprint = FingerprintFactory.create(request);
            identityServiceProvider.get().setFingerprint(fingerprint);

            // get token if it exists
            if (token != null) {
                identityServiceProvider.get().retrieveToken(token);
            }

            // check for validity and renew
            if (identityServiceProvider.get().isTokenValid()) {
                identityServiceProvider.get().renewToken();
            }
        }
    }

    void validateToken(String contextPath, String requestURI) {
        String servicePath = getServicePath(contextPath, requestURI);
        if (!UNENCRYPTED_PATHS.contains(servicePath) && !identityServiceProvider.get().isTokenValid()) {
            throw new ForbiddenException("Token is not valid");
        }
    }


    /**
     * Parses service path from request URL
     *
     * @param requestURI request URI
     * @return a service path like {@code /auth}
     */
     String getServicePath(String contextPath, String requestURI) {
        if (requestURI.length() <= 1) return requestURI;
        String result = requestURI;
        if (result.startsWith(contextPath)) result = result.substring(contextPath.length());

        int slashIndex = result.indexOf("/", 1);
        if (slashIndex == -1) return result;

        return result.substring(0, slashIndex);
    }

}
