package wappsto.rest.session;

import org.glassfish.jersey.client.ClientConfig;
import javax.ws.rs.client.*;

public abstract class Session {
    protected String id;
    protected Client client;
    protected WebTarget service;

    public Session() {
        client = ClientBuilder.newClient(new ClientConfig());
        service = client.target("https://qa.wappsto.com/services/2.1/");
    }
}
