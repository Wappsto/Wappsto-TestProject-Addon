package wappsto.rest.iot;

import wappsto.rest.iot.model.CreatorResponse;
import wappsto.rest.request.API;
import wappsto.rest.request.Request;
import wappsto.rest.session.Session;

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

    public CreatorResponse create() throws Exception {
        return new Request.Builder(session.service)
            .atEndPoint(API.CREATOR)
            .withBody("{}")
            .post(session.id)
            .readEntity(CreatorResponse.class);
    }
}
