package pl.cezaryregec.resources;

import pl.cezaryregec.auth.session.IdentityService;
import pl.cezaryregec.exception.APIException;
import pl.cezaryregec.user.UserService;
import pl.cezaryregec.user.models.ChangePasswordRequest;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class User {

    private final UserService userService;
    private final IdentityService identityService;

    @Inject
    public User(UserService userService, IdentityService identityService) {
        this.userService = userService;
        this.identityService = identityService;
    }

    @POST
    public Response createUser(pl.cezaryregec.user.models.User user) throws APIException {
        userService.create(user);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response getCurrentUser() {
        Optional<pl.cezaryregec.user.models.User> boundUser = identityService.getBoundUser();

        if (boundUser.isPresent()) {
            return Response.ok(boundUser.get()).build();
        }

        throw new ForbiddenException("Not logged in");
    }

    @PUT
    public Response changePassword(ChangePasswordRequest request) throws APIException {
        Optional<pl.cezaryregec.user.models.User> boundUser = identityService.getBoundUser();

        if (boundUser.isPresent() && boundUser.get().getUsername().equals(request.getUsername())) {
            userService.changePassword(request);
            return Response.ok().build();
        }

        throw new ForbiddenException("Wrong credentials");
    }
}
