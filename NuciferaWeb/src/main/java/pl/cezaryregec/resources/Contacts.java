package pl.cezaryregec.resources;

import pl.cezaryregec.auth.session.IdentityService;
import pl.cezaryregec.user.UserService;
import pl.cezaryregec.user.models.User;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("contacts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Contacts {

    private final IdentityService identityService;
    private final UserService userService;

    @Inject
    public Contacts(IdentityService identityService, UserService userService) {
        this.identityService = identityService;
        this.userService = userService;
    }

    @GET
    public Response getContacts() {
        Optional<User> boundUser = identityService.getBoundUser();

        if (boundUser.isPresent()) {
            return Response.ok(boundUser.get().getContacts()).build();
        }

        throw new ForbiddenException("Not logged in");
    }

    @PUT
    public Response addContact(@QueryParam("id") String userId) {
        userService.addContact(userId);

        return Response.ok().build();
    }
}
