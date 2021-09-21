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

    public CreatorResponse getCreator() throws Exception {
        return new Request.Builder(session.service)
            .atEndPoint(API._2_1)
            .atEndPoint(API.CREATOR)
            .withBody("{}")
            .post(session.id)
            .readEntity(CreatorResponse.class);
    }

    public Network create() throws Exception {
        return new Request.Builder(session.service)
            .atEndPoint(API._2_0)
            .atEndPoint(API.NETWORK)
            .withBody("{}")
            .post(session.id)
            .readEntity(NetworkResponse.class)
            .network;
    }

    public Network fetch(String id) throws Exception {
        return new Request.Builder(session.service)
            .atEndPoint(API.NETWORK)
            .atEndPoint(id)
            .get(session.id)
            .readEntity(NetworkResponse.class)
            .network;
    }
}
