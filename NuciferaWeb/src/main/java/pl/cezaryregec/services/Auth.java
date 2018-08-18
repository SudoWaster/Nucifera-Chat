package pl.cezaryregec.services;

import pl.cezaryregec.auth.service.PostAuthQuery;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
public class Auth {

    @Inject
    public Auth() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postAuth(PostAuthQuery postAuthQuery) {
        return Response.ok().build();
    }
}
