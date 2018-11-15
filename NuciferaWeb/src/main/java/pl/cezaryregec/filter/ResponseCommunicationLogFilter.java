package pl.cezaryregec.filter;

import pl.cezaryregec.logger.CommunicationLogger;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import com.google.inject.Inject;

@Provider
@Priority(Priorities.USER)
public class ResponseCommunicationLogFilter implements ContainerResponseFilter {

    private final CommunicationLogger communicationLogger;

    @Inject
    public ResponseCommunicationLogFilter(CommunicationLogger communicationLogger) {this.communicationLogger = communicationLogger;}

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        communicationLogger.log(responseContext);
    }
}
