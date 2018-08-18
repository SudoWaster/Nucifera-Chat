package pl.cezaryregec.services;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
public class Auth {

    @POST
    public Response postAuth(
            @FormParam("username") String username,
            @FormParam("password") String password) {

        return Response.ok().build();
    }

    @DELETE
    public Response deleteToken(
            @QueryParam("token") String token
    ) {

        return Response.ok().build();
    }
}
