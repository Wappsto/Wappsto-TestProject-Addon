package actions.network;

import wappsto.network.*;
import wappsto.rest.network.*;
import wappsto.rest.session.*;

public class CreateNetworkController {
    private NetworkService service;

    public CreateNetworkController(
        String sessionId,
        String target
    ) throws Exception
    {
        this(new RestNetworkService(new RestUser(sessionId, target)));
    }

    public CreateNetworkController(NetworkService service) {
        this.service = service;
    }

    public String execute() throws Exception {
        return service.create().id;
    }
}
