package pl.cezaryregec.resources;

import pl.cezaryregec.message.MessageService;
import pl.cezaryregec.message.model.Message;
import pl.cezaryregec.message.model.MessageSendRequest;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Messages {

    private final MessageService messageService;

    @Inject
    public Messages(MessageService messageService) {
        this.messageService = messageService;
    }

    @GET
    public Response getMessages() {
        List<Message> messages = messageService.getMessages();
        return Response.ok(messages).build();
    }

    @GET
    @Path("{uid}")
    public Response getThread(
            @PathParam("uid") Long userId,
            @DefaultValue("0") @QueryParam("part") Integer part
    ) {
        List<Message> messages = messageService.getLastMessages(userId, part);
        return Response.ok(messages).build();
    }

    @POST
    public Response postMessage(MessageSendRequest message) {
        messageService.postMessage(message);
        return Response.ok().build();
    }
}
