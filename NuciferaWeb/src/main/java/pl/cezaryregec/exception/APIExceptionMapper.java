package pl.cezaryregec.exception;

import pl.cezaryregec.ApplicationLogger;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class APIExceptionMapper extends Throwable implements ExceptionMapper<Throwable> {

    private static final long serialVersionUID = 5089659007201514628L;

    @Override
    public Response toResponse(Throwable exception) {
        ApplicationLogger.log(exception);
        APIException apiException = new APIException(exception.getMessage());
        if (exception instanceof ClientErrorException) {
            ClientErrorException clientError = (ClientErrorException) exception;
            apiException.setErrorCode(clientError.getResponse().getStatus());
        }
        return Response.status(apiException.getErrorCode()).entity(apiException).build();
    }
}
