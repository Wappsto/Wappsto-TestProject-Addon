package wappsto.rest.session.core;

import org.glassfish.jersey.client.ClientConfig;
import wappsto.rest.request.API;
import wappsto.rest.request.Request;
import wappsto.rest.session.model.Credentials;
import wappsto.rest.session.model.SessionResponse;

import javax.ws.rs.client.*;

public abstract class Session {
    public final String id;
    protected Client client;
    public final WebTarget service;

    /**
     * The base class for API sessions
     * @param credentials   Login credentials
     * @param serviceUrl    URL to the API
     * @throws Exception
     */
    public Session(Credentials credentials, String serviceUrl) throws Exception {
        createClient(serviceUrl);
        service = client.target(serviceUrl);

        id = new Request.Builder(service)
            .atEndPoint(API.SESSION)
            .withBody(credentials)
            .post()
            .readEntity(SessionResponse.class).sessionId.id;
    }

    /**
     * Instantiates a session object from an existing session ID
     * @param id            Session ID
     * @param serviceUrl    URL to the API
     */
    public Session(String id, String serviceUrl) {
        createClient(serviceUrl);
        service = client.target(serviceUrl);
        this.id = id;
    }

    protected void createClient(String serviceUrl) {
        client = ClientBuilder.newClient(new ClientConfig());
    }
}
