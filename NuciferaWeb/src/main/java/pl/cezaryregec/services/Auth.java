package pl.cezaryregec.services;

import pl.cezaryregec.auth.AuthResponseFactory;
import pl.cezaryregec.auth.service.PostAuth;
import pl.cezaryregec.auth.service.PostAuthQuery;

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
    public Response postAuth(PostAuthQuery postAuthQuery) {
        PostAuth postAuth = authResponseFactory.create(postAuthQuery.authState);
        return Response.ok(postAuth.execute(postAuthQuery)).build();
    }
}
