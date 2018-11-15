package pl.cezaryregec.filter;

import pl.cezaryregec.logger.CommunicationLogger;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import com.google.inject.Inject;

@Provider
@Priority(Priorities.USER)
public class RequestCommunicationLogFilter implements ContainerRequestFilter {

    private final CommunicationLogger communicationLogger;

    @Inject
    public RequestCommunicationLogFilter(CommunicationLogger communicationLogger) {
        this.communicationLogger = communicationLogger;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        communicationLogger.log(requestContext);
    }
}
