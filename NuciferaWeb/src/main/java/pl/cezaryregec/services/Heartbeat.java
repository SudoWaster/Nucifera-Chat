package pl.cezaryregec.services;

import pl.cezaryregec.exception.APIException;
import pl.cezaryregec.heartbeat.service.HeartbeatService;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("heartbeat")
@Produces(MediaType.APPLICATION_JSON)
public class Heartbeat {

    private final HeartbeatService heartbeatService;

    @Inject
    public Heartbeat(HeartbeatService heartbeatService) {
        this.heartbeatService = heartbeatService;
    }

    @POST
    public Response postHeartbeat() throws APIException {
        heartbeatService.rememberLiveClient();
        return Response.ok().build();
    }
}
