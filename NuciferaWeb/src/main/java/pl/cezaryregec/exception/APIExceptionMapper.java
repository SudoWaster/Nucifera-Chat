package pl.cezaryregec.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.cezaryregec.config.ConfigSupplier;
import pl.cezaryregec.logger.ApplicationLogger;

import javax.inject.Inject;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class APIExceptionMapper extends Throwable implements ExceptionMapper<Throwable> {

    private static final long serialVersionUID = 5089659007201514628L;

    private final ApplicationLogger applicationLogger;
    private final ConfigSupplier configSupplier;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    public APIExceptionMapper(
            ApplicationLogger applicationLogger,
            ConfigSupplier configSupplier
    ) {
        this.applicationLogger = applicationLogger;
        this.configSupplier = configSupplier;
    }

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(Throwable exception) {
        applicationLogger.log(exception);
        APIException apiException = new APIException(exception.getMessage());

        if (exception instanceof ClientErrorException) {
            ClientErrorException clientError = (ClientErrorException) exception;
            apiException.setErrorCode(clientError.getResponse().getStatus());
        }

        if (exception instanceof APIException) {
            apiException.setErrorCode(((APIException) exception).getErrorCode());
        }

        if (configSupplier.get().getDebug().getVerbose()) {
            return prepareResponse(apiException);
        }

        CleanAPIException cleanAPIException = new CleanAPIException(apiException.getMessage(), apiException.getErrorCode());
        return prepareResponse(cleanAPIException);
    }

    private Response prepareResponse(APIException apiException) {
        String result;
        try {
            result = objectMapper.writeValueAsString(apiException);
        } catch (JsonProcessingException e) {
            result = apiException.toString();
        }
        return Response
                .status(apiException.getErrorCode())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(result)
                .build();
    }
}
