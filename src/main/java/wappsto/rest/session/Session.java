package wappsto.rest.session;

import org.glassfish.jersey.client.ClientConfig;
import wappsto.rest.request.API;
import wappsto.rest.request.Request;
import wappsto.rest.session.model.Credentials;
import wappsto.rest.session.model.SessionResponse;

import javax.ws.rs.client.*;

public abstract class Session {
    public final String id;
    protected Client client;
    protected WebTarget service;

    public Session(Credentials credentials, String serviceUrl) throws Exception {
        createClient(serviceUrl);

        id = new Request.Builder(service)
            .atEndPoint(API.SESSION)
            .withBody(credentials)
            .post()
            .readEntity(SessionResponse.class).sessionId.id;
    }

    public Session(String id, String serviceUrl) {
        createClient(serviceUrl);
        this.id = id;
    }

    protected void createClient(String serviceUrl) {
        client = ClientBuilder.newClient(new ClientConfig());
        service = client.target(serviceUrl);
    }
}
