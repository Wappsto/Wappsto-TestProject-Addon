package wappsto.rest.session;

import wappsto.rest.request.API;
import wappsto.rest.request.Request;
import wappsto.rest.session.core.Session;

public class Network {
    private Session session;

    public Network(Session session) {
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
}
