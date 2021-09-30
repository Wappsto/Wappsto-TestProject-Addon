package actions.network;

import wappsto.network.*;
import wappsto.rest.network.*;
import wappsto.rest.session.*;

public class ShareNetworkController {

    private NetworkService service;
    private String network;
    private String other;

    public ShareNetworkController(
        String sessionId,
        String target,
        String network,
        String other
    ) throws Exception {
        this(
            new RestNetworkService(new RestUser(sessionId, target)),
            network,
            other
        );
    }

    public ShareNetworkController(
        NetworkService service,
        String network,
        String other
    ) {

        this.service = service;
        this.network = network;
        this.other = other;
    }

    public void execute() throws Exception {
        service.share(network, other);
    }
}
