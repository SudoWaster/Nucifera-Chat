package pl.cezaryregec.resources;

import pl.cezaryregec.auth.session.IdentityService;
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

    @Inject
    public Contacts(IdentityService identityService) {
        this.identityService = identityService;
    }

    @GET
    public Response getContacts() {
        Optional<User> boundUser = identityService.getBoundUser();

        if (boundUser.isPresent()) {
            return Response.ok(boundUser.get().getContacts()).build();
        }

        throw new ForbiddenException("Not logged in");
    }
}
