package pl.cezaryregec.filter;

import com.google.inject.Provider;
import pl.cezaryregec.auth.session.IdentityService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

public class TokenInitializer {

    @Context
    HttpServletRequest request;

    private final Provider<IdentityService> identityServiceProvider;

    @Inject
    public TokenInitializer(Provider<IdentityService> identityServiceProvider) {
        this.identityServiceProvider = identityServiceProvider;
    }

    public void init(String token) {
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
}
