package pl.cezaryregec.resources;

import pl.cezaryregec.auth.AuthResponseFactory;
import pl.cezaryregec.auth.models.LoginQuery;
import pl.cezaryregec.auth.service.LoginService;
import pl.cezaryregec.auth.service.PostAuth;
import pl.cezaryregec.auth.models.PostAuthQuery;
import pl.cezaryregec.exception.APIException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Auth {

    private final AuthResponseFactory authResponseFactory;
    private final LoginService loginService;

    @Inject
    public Auth(AuthResponseFactory authResponseFactory, LoginService loginService) {
        this.authResponseFactory = authResponseFactory;
        this.loginService = loginService;
    }

    @POST
    public Response initAuth(PostAuthQuery postAuthQuery) throws APIException {
        PostAuth postAuth = authResponseFactory.create(postAuthQuery.getState());
        return Response.ok(postAuth.execute(postAuthQuery)).build();
    }

    @PUT
    public Response login(LoginQuery loginQuery) throws APIException {
        loginService.login(loginQuery);
        return Response.ok().build();
    }

    @DELETE
    public Response signOut() {
        loginService.signOut();
        return Response.ok().build();
    }
}
