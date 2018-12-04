package pl.cezaryregec.filter;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.commons.io.IOUtils;
import pl.cezaryregec.auth.session.IdentityService;
import pl.cezaryregec.config.ConfigSupplier;
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
    HttpServletRequest request;

    private final Provider<IdentityService> identityServiceProvider;
    private final SymmetricDecryptor decryptor;
    private final SecurityLogger securityLogger;
    private final ConfigSupplier configSupplier;

    @Inject
    public RequestEncryptedReaderInterceptor(
            Provider<IdentityService> identityServiceProvider,
            SymmetricDecryptor decryptor,
            SecurityLogger securityLogger,
            ConfigSupplier configSupplier
    ) {
        this.identityServiceProvider = identityServiceProvider;
        this.decryptor = decryptor;
        this.securityLogger = securityLogger;
        this.configSupplier = configSupplier;
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        String fingerprint = getFingerprint();
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

        boolean hasCipherSpec = identityServiceProvider.get().hasCipherSpec();
        String servicePath = getServicePath(request.getRequestURI());
        boolean isEncryptionEnabled = configSupplier.get().getSecurity().getAdditionalEncryption();
        boolean mustBeEncrypted = isEncryptionEnabled && !UNENCRYPTED_PATHS.contains(servicePath);

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
        return request.getRemoteAddr() + "\n" + request.getRemoteHost() + "\n" + request.getHeader("User-Agent");
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
        byte[] input = IOUtils.toByteArray(inputStream);

        if (configSupplier.get().getSecurity().getBase64()) {
            return getDecryptedBase64(input);
        } else {
            return getDecryptedBytes(input);
        }
    }

    /**
     * Decrypt data sent as String in a Base64 format
     *
     * @param input byte array of a String with Base64 message
     * @return decrypted stream
     * @throws APIException when decryption error happens
     */
    private ByteArrayInputStream getDecryptedBase64(byte[] input) throws APIException {
        String stringInput = new String(input, StandardCharsets.UTF_8);
        String challenge = identityServiceProvider.get().getChallenge();
        try {
            String decrypted = decryptor.decrypt(stringInput, challenge);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decrypted.getBytes(StandardCharsets.UTF_8));
            return byteArrayInputStream;
        } catch (Exception e) {
            throw new APIException(e);
        }
    }

    /**
     * Decrypt raw data sent as bytes
     *
     * @param input raw encrypted input
     * @return decrypted stream
     * @throws APIException when decryption error happens
     */
    private ByteArrayInputStream getDecryptedBytes(byte[] input) throws APIException {
        byte[] challenge = identityServiceProvider.get().getChallenge().getBytes(StandardCharsets.UTF_8);
        try {
            byte[] decrypted = decryptor.decrypt(input, challenge);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decrypted);
            return byteArrayInputStream;
        } catch (Exception e) {
            throw new APIException(e);
        }
    }
}
