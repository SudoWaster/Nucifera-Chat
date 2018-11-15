package pl.cezaryregec.exception;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import pl.cezaryregec.config.ConfigSupplier;
import pl.cezaryregec.logger.ApplicationLogger;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Singleton
public class APIExceptionMapper extends Throwable implements ExceptionMapper<Throwable> {

    private static final long serialVersionUID = 5089659007201514628L;

    private final ApplicationLogger applicationLogger;
    private final ConfigSupplier configSupplier;

    @Inject
    public APIExceptionMapper(
            ApplicationLogger applicationLogger,
            ConfigSupplier configSupplier
    ) {
        this.applicationLogger = applicationLogger;
        this.configSupplier = configSupplier;
    }

    @Override
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
            return Response.status(apiException.getErrorCode()).entity(apiException).build();
        }

        CleanAPIException cleanAPIException = new CleanAPIException(apiException.getMessage(), apiException.getErrorCode());
        return Response.status(cleanAPIException.getErrorCode()).entity(cleanAPIException).build();
    }
}
