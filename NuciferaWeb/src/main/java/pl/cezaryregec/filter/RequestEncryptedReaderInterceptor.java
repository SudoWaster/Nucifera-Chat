package pl.cezaryregec.filter;

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

        if (identityServiceProvider.get().isTokenValid()) {
            identityServiceProvider.get().renewToken();
        }

        String tokenId = context.getHeaders().getFirst("X-Nucifera-Token");
        if (tokenId != null) {
            identityServiceProvider.get().retrieveToken(tokenId);
        }

        if (identityServiceProvider.get().hasCipherSpec()) {
            try {
                InputStream decryptedStream = getDecrypted(context);
                context.setMediaType(MediaType.APPLICATION_JSON_TYPE);
                context.setInputStream(decryptedStream);
            } catch (APIException e) {
                securityLogger.log(e);
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
