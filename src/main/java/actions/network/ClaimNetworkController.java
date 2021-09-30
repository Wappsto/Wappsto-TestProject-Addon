package actions.network;

import wappsto.network.*;
import wappsto.rest.network.*;
import wappsto.rest.session.*;

public class ClaimNetworkController {
    private NetworkService service;
    private String network;

    public ClaimNetworkController(
        String sessionId,
        String target,
        String network
    ) throws Exception {
        this(
            new RestNetworkService(new RestUser(sessionId, target)),
            network
        );
    }

    public ClaimNetworkController(NetworkService service, String network) {
        this.service = service;
        this.network = network;
    }

    public void execute() throws Exception {
        service.claim(network);
    }
}
