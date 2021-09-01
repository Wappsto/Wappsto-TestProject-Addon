package wappsto.rest.session;

import org.glassfish.jersey.client.ClientConfig;
import javax.ws.rs.client.*;

public abstract class Session {
    protected String id;
    protected Client client;
    protected WebTarget api;

    public Session() {
        client = ClientBuilder.newClient(new ClientConfig());
        api = client.target("https://qa.wappsto.com/services/2.1/");
    }
}
