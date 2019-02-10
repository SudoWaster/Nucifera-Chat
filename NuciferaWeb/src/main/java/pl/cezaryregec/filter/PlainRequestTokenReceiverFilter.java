package pl.cezaryregec.filter;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import java.io.IOException;

/**
 * Additional filter to retrieve token when request body is empty
 * <p>
 * Empty request body does not trigger RequestEncryptedReaderInterceptor which
 * triggers token retrieval
 */
public class PlainRequestTokenReceiverFilter implements ContainerRequestFilter {

    private final TokenUtils tokenUtils;

    @Context
    HttpServletRequest request;
    @Context
    ServletContext servletContext;

    @Inject
    public PlainRequestTokenReceiverFilter(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        String token = context.getHeaders().getFirst("X-Nucifera-Token");
        tokenUtils.init(request, token);
        String requestURI = request.getRequestURI();
        tokenUtils.validateToken(servletContext.getContextPath(), requestURI);
    }
}
