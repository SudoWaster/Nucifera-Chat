package pl.cezaryregec.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Provider;
import pl.cezaryregec.auth.session.IdentityService;
import pl.cezaryregec.config.ConfigSupplier;
import pl.cezaryregec.crypt.SymmetricEncryptor;
import pl.cezaryregec.exception.APIException;
import pl.cezaryregec.logger.ApplicationLogger;
import pl.cezaryregec.logger.SecurityLogger;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
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

    @Context
    HttpServletResponse response;

    private final Provider<IdentityService> identityService;
    private final SymmetricEncryptor symmetricEncryptor;
    private final ApplicationLogger applicationLogger;
    private final SecurityLogger securityLogger;
    private final ConfigSupplier configSupplier;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    public ResponseEncryptedWriterInterceptor(
            Provider<IdentityService> identityService,
            SymmetricEncryptor symmetricEncryptor,
            ApplicationLogger applicationLogger,
            SecurityLogger securityLogger,
            ConfigSupplier configSupplier
    ) {
        this.identityService = identityService;
        this.symmetricEncryptor = symmetricEncryptor;
        this.applicationLogger = applicationLogger;
        this.securityLogger = securityLogger;
        this.configSupplier = configSupplier;
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        boolean hasCipherSpec = identityService.get().hasCipherSpec();
        boolean isJson = MediaType.APPLICATION_JSON_TYPE.equals(context.getMediaType());

        if (hasCipherSpec && isJson) {
            try {
                tryEncryptContext(context);
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
     * Tries to encrypt outgoing communication to base64 or raw bytes, depending on config
     *
     * @param context {@link WriterInterceptorContext} that contains original entity
     * @throws APIException            when there is a problem with encryption
     * @throws JsonProcessingException when there is a problem with serialization
     */
    private void tryEncryptContext(WriterInterceptorContext context) throws APIException, IOException {
        if (configSupplier.get().getSecurity().getBase64()) {
            String entity = getEncryptedBase64(context);
            context.setMediaType(MediaType.TEXT_PLAIN_TYPE);
            context.setType(String.class);
            context.setType(String.class);
            context.setEntity(entity);
            response.setContentType(MediaType.TEXT_PLAIN);
        } else {
            byte[] entity = getEncryptedBytes(context);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(entity);
            context.setMediaType(MediaType.APPLICATION_OCTET_STREAM_TYPE);
            context.setType(ByteArrayOutputStream.class);
            context.setGenericType(OutputStream.class);
            context.setEntity(outputStream);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        }
    }

    /**
     * Encrypts communication using challenge from memory to a Byte64 String
     *
     * @param context {@link WriterInterceptorContext} that contains original entity
     * @return an encrypted Byte64 coded String
     * @throws APIException            when there is a problem with encryption
     * @throws JsonProcessingException when there is a problem with serialization
     */
    private String getEncryptedBase64(WriterInterceptorContext context) throws JsonProcessingException, APIException {
        String input = objectMapper.writeValueAsString(context.getEntity());
        String challenge = identityService.get().getChallenge();

        try {
            return symmetricEncryptor.encrypt(input, challenge);
        } catch (Exception e) {
            securityLogger.log(e);
            throw new APIException(e);
        }
    }

    /**
     * Encrypts communication using challenge from memory to a raw byte array
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
