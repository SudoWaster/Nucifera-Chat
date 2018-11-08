package pl.cezaryregec.services;

import pl.cezaryregec.auth.AuthResponseFactory;
import pl.cezaryregec.auth.service.PostAuth;
import pl.cezaryregec.auth.models.PostAuthQuery;
import pl.cezaryregec.exception.APIException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
public class Auth {

    private final AuthResponseFactory authResponseFactory;

    @Inject
    public Auth(AuthResponseFactory authResponseFactory) {
        this.authResponseFactory = authResponseFactory;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postAuth(PostAuthQuery postAuthQuery) throws APIException {
        PostAuth postAuth = authResponseFactory.create(postAuthQuery.getState());
        return Response.ok(postAuth.execute(postAuthQuery)).build();
    }
}
