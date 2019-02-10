package pl.cezaryregec.filter;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provider;
import org.apache.commons.io.IOUtils;
import pl.cezaryregec.auth.session.IdentityService;
import pl.cezaryregec.config.ConfigSupplier;
import pl.cezaryregec.crypt.SymmetricDecryptor;
import pl.cezaryregec.exception.APIException;
import pl.cezaryregec.logger.SecurityLogger;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.ServletContext;
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
    HttpServletRequest request;
    @Context
    ServletContext servletContext;

    private final Provider<IdentityService> identityServiceProvider;
    private final SymmetricDecryptor decryptor;
    private final SecurityLogger securityLogger;
    private final ConfigSupplier configSupplier;
    private final TokenUtils tokenUtils;

    @Inject
    public RequestEncryptedReaderInterceptor(
            Provider<IdentityService> identityServiceProvider,
            SymmetricDecryptor decryptor,
            SecurityLogger securityLogger,
            ConfigSupplier configSupplier,
            TokenUtils tokenUtils) {
        this.identityServiceProvider = identityServiceProvider;
        this.decryptor = decryptor;
        this.securityLogger = securityLogger;
        this.configSupplier = configSupplier;
        this.tokenUtils = tokenUtils;
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        String token = context.getHeaders().getFirst("X-Nucifera-Token");
        tokenUtils.init(request, token);

        boolean hasCipherSpec = identityServiceProvider.get().hasCipherSpec();
        String servicePath = tokenUtils.getServicePath(servletContext.getContextPath(), request.getRequestURI());
        boolean isEncryptionEnabled = configSupplier.get().getSecurity().getAdditionalEncryption();
        boolean mustBeEncrypted = !TokenUtils.UNENCRYPTED_PATHS.contains(servicePath);

        if (isEncryptionEnabled && (hasCipherSpec || !mustBeEncrypted)) {
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
