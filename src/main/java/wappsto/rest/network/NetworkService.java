package wappsto.rest.network;

import wappsto.rest.network.model.*;
import wappsto.rest.request.*;
import wappsto.rest.session.*;

public class NetworkService {
    private Session session;

    public NetworkService(Session session) {
        this.session = session;
    }

    /**
     * Claim an already existing network by its UUID
     * @param id
     * @throws Exception
     */
    public void claim(String id) throws Exception {
        new Request.Builder(session.service)
            .atEndPoint(API.NETWORK)
            .atEndPoint(id)
            .withBody("{}")
            .post(session.id);
    }

    public NetworkCreatorResponse create() throws Exception {
        return new Request.Builder(session.service)
            .atEndPoint(API.CREATOR)
            .withBody("{}")
            .post(session.id)
            .readEntity(NetworkCreatorResponse.class);
    }
}
