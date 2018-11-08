package pl.cezaryregec.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Provider;
import pl.cezaryregec.auth.session.IdentityService;
import pl.cezaryregec.crypt.SymmetricEncryptor;
import pl.cezaryregec.exception.APIException;
import pl.cezaryregec.logger.ApplicationLogger;
import pl.cezaryregec.logger.SecurityLogger;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

@javax.ws.rs.ext.Provider
@Priority(Priorities.AUTHENTICATION)
public class ResponseEncryptedWriterInterceptor implements WriterInterceptor {

    private final Provider<IdentityService> identityService;
    private final SymmetricEncryptor symmetricEncryptor;
    private final ApplicationLogger applicationLogger;
    private final SecurityLogger securityLogger;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    public ResponseEncryptedWriterInterceptor(Provider<IdentityService> identityService, SymmetricEncryptor symmetricEncryptor, ApplicationLogger applicationLogger, SecurityLogger securityLogger) {
        this.identityService = identityService;
        this.symmetricEncryptor = symmetricEncryptor;
        this.applicationLogger = applicationLogger;
        this.securityLogger = securityLogger;
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        boolean hasCipherSpec = identityService.get().hasCipherSpec();
        boolean isJson = MediaType.APPLICATION_JSON_TYPE.equals(context.getMediaType());

        if (hasCipherSpec && isJson) {
            try {
                byte[] entity = getEncryptedBytes(context);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                outputStream.write(entity);
                context.setMediaType(MediaType.APPLICATION_OCTET_STREAM_TYPE);
                context.setType(ByteArrayOutputStream.class);
                context.setGenericType(OutputStream.class);
                context.setEntity(outputStream);
            } catch (Exception e) {
                applicationLogger.log(e);
                // if we cannot encrypt this message,
                // we shouldn't send it to the client
                identityService.get().invalidate();
                APIException exception = new APIException(e.getMessage());
                context.setMediaType(MediaType.APPLICATION_JSON_TYPE);
                context.setType(APIException.class);
                context.setGenericType(Serializable.class);
                context.setEntity(exception);
            }
        }

        context.proceed();
    }

    /**
     * Encrypts communication using challenge from memory
     *
     * @param context {@link WriterInterceptorContext} that contains original entity
     * @return an encrypted byte array
     * @throws APIException            when there is a problem with encryption
     * @throws JsonProcessingException when there is a problem with serialization
     */
    private byte[] getEncryptedBytes(WriterInterceptorContext context) throws APIException, JsonProcessingException {
        String plainEntity = objectMapper.writeValueAsString(context.getEntity());
        byte[] entity = plainEntity.getBytes(StandardCharsets.UTF_8);
        String stringChallenge = identityService.get().getChallenge();
        byte[] challenge = stringChallenge.getBytes(StandardCharsets.UTF_8);

        try {
            return symmetricEncryptor.encrypt(entity, challenge);
        } catch (Exception e) {
            securityLogger.log(e);
            throw new APIException(e);
        }
    }
}
