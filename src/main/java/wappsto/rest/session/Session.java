package wappsto.rest.session;

import org.glassfish.jersey.client.ClientConfig;
import javax.ws.rs.client.*;

public abstract class Session {
    protected String id;
    protected Client client;
    protected WebTarget service;

    public Session(String serviceUrl) {
        client = ClientBuilder.newClient(new ClientConfig());
        service = client.target(serviceUrl);
    }
}
