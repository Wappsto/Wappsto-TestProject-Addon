package wappsto.rest.session;

import org.glassfish.jersey.client.ClientConfig;
import wappsto.rest.request.API;
import wappsto.rest.request.Request;
import wappsto.rest.session.model.Credentials;
import wappsto.rest.session.model.SessionResponse;

import javax.ws.rs.client.*;

public abstract class Session {
    public String id;
    protected Client client;
    protected WebTarget service;

    public Session(String serviceUrl) {
        client = ClientBuilder.newClient(new ClientConfig());
        service = client.target(serviceUrl);
    }

    public Session(Credentials credentials, String serviceUrl) throws Exception {
        this(serviceUrl);

        id = new Request.Builder(service)
            .atEndPoint(API.SESSION)
            .withBody(credentials)
            .post()
            .readEntity(SessionResponse.class).sessionId.id;
    }
}
