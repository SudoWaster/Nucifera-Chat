package pl.cezaryregec.logger;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class CommunicationLogger {
    private static final Logger LOGGER = LogManager.getLogger("communication");

    /**
     * Logs communication
     *
     * @param requestContext  a {@link ContainerRequestContext} from filter
     * @param responseContext a {@link ContainerResponseContext} from filter
     * @throws IOException when entity InputStream parsing error occurs
     */
    public void log(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        Level level = Level.ERROR;
        if (isSuccessful(responseContext.getStatusInfo())) {
            level = Level.INFO;
        }

        String request = prepareRequestLog(requestContext);
        String response = prepareResponseLog(responseContext);

        LOGGER.log(level, "[ REQUEST ] " + request);
        LOGGER.log(level, "[ RESPONSE ] " + response);
    }

    /**
     * Checks if response did not contain errors
     *
     * @param status status info from {@link ContainerResponseContext}
     * @return {@code true} if status is in HTTP 2xx family
     */
    private boolean isSuccessful(Response.StatusType status) {
        return Response.Status.Family.SUCCESSFUL.equals(status.getFamily());
    }

    /**
     * Returns formatted request log
     *
     * @param requestContext a request
     * @return formatted output
     * @throws IOException when entity InputStream parsing error occurs
     */
    public String prepareRequestLog(ContainerRequestContext requestContext) throws IOException {
        String request = requestContext.getMethod() + ": " + requestContext.getUriInfo().getRequestUri() + "\n";
        request += formatStringHeaders(requestContext.getHeaders());

        if (requestContext.hasEntity()) {
            InputStream entityStream = requestContext.getEntityStream();
            request += IOUtils.toString(entityStream, StandardCharsets.UTF_8.name());
        }

        return request;
    }

    /**
     * Returns formatted response log
     *
     * @param responseContext a response
     * @return formatted output
     */
    public String prepareResponseLog(ContainerResponseContext responseContext) {
        String response = "Status: " + responseContext.getStatus() + "\n";
        response += formatStringHeaders(responseContext.getStringHeaders());

        if (responseContext.hasEntity()) {
            response += responseContext.getEntity().toString();
        }

        return response;
    }

    /**
     * Returns formatted HTTP headers from {@code MultivaluedMap<String, String>}
     *
     * @param headers headers from request or response
     * @return formatted output
     */
    private String formatStringHeaders(MultivaluedMap<String, String> headers) {
        String result = "";
        for (Map.Entry<String, List<String>> header : headers.entrySet()) {
            String entries = String.join(", ", header.getValue());
            result += header.getKey() + ": " + entries + "\n";
        }

        return result;
    }
}