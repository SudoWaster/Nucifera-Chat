package pl.cezaryregec.filter;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.commons.io.IOUtils;
import pl.cezaryregec.auth.session.IdentityService;
import pl.cezaryregec.crypt.SymmetricDecryptor;
import pl.cezaryregec.exception.APIException;
import pl.cezaryregec.logger.SecurityLogger;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@javax.ws.rs.ext.Provider
@Priority(Priorities.AUTHENTICATION)
public class RequestEncryptedReaderInterceptor implements ReaderInterceptor {

    private static final ImmutableSet UNENCRYPTED_PATHS;

    static {
        UNENCRYPTED_PATHS = ImmutableSet.of("/auth");
    }

    @Context
    private HttpServletRequest request;

    private final Provider<IdentityService> identityServiceProvider;
    private final SymmetricDecryptor decryptor;
    private final SecurityLogger securityLogger;

    @Inject
    public RequestEncryptedReaderInterceptor(Provider<IdentityService> identityServiceProvider, SymmetricDecryptor decryptor, SecurityLogger securityLogger) {
        this.identityServiceProvider = identityServiceProvider;
        this.decryptor = decryptor;
        this.securityLogger = securityLogger;
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        String fingerprint = getFingerprint();
        identityServiceProvider.get().setFingerprint(fingerprint);

        String tokenId = context.getHeaders().getFirst("X-Nucifera-Token");
        if (tokenId != null) {
            identityServiceProvider.get().retrieveToken(tokenId);
        }

        if (identityServiceProvider.get().isTokenValid()) {
            identityServiceProvider.get().renewToken();
        }

        boolean hasCipherSpec = identityServiceProvider.get().hasCipherSpec();
        String servicePath = getServicePath(request.getRequestURI());
        boolean mustBeEncrypted = !UNENCRYPTED_PATHS.contains(servicePath);

        if (hasCipherSpec || !mustBeEncrypted) {
            try {
                InputStream decryptedStream = getDecrypted(context);
                context.setMediaType(MediaType.APPLICATION_JSON_TYPE);
                context.setInputStream(decryptedStream);
            } catch (APIException e) {
                securityLogger.log(e);
                // do not parse that request
                context.setInputStream(null);
                throw new WebApplicationException(e);
            }
        }

        return context.proceed();
    }

    /**
     * Creates client fingerprint
     *
     * @return String of distinct user client data
     */
    private String getFingerprint() {
        return request.getRemoteAddr() + request.getRemoteHost() + request.getHeader("User-Agent");
    }

    /**
     * Parses service path from request URL
     *
     * @param requestURI request URI
     * @return a service path like {@code /auth}
     */
    private String getServicePath(String requestURI) {
        if (requestURI.length() <= 1) return requestURI;

        int slashIndex = requestURI.indexOf("/", 1);
        if (slashIndex == -1) return requestURI;

        return requestURI.substring(0, slashIndex);
    }

    /**
     * Create decrypted stream from context
     *
     * @param context
     * @return decrypted {@link InputStream}
     * @throws IOException  when InputStream error happens
     * @throws APIException when decryption error happens
     */
    private InputStream getDecrypted(ReaderInterceptorContext context) throws IOException, APIException {
        InputStream inputStream = context.getInputStream();
        byte[] raw = IOUtils.toByteArray(inputStream);
        byte[] challenge = identityServiceProvider.get().getChallenge().getBytes(StandardCharsets.UTF_8);
        try {
            byte[] decrypted = decryptor.decrypt(raw, challenge);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decrypted);
            return byteArrayInputStream;
        } catch (Exception e) {
            throw new APIException(e);
        }
    }
}
