package wappsto.rest;

import org.glassfish.jersey.client.ClientConfig;
import wappsto.rest.model.User;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;

public class UserSession {
    private String id;
    private Client client;
    private WebTarget api;

    public UserSession(String id) {
        this.id = id;

        client = ClientBuilder.newClient(new ClientConfig());
        api = client.target("https://qa.wappsto.com/services/2.1/");

    }

    public User fetchUser() throws Exception{

        Response response = api.path("user")
            .request("application/json")
            .header("X-Session", id)
            .get();

        if (response.getStatus() != 200) {
            throw new Exception(response.getStatusInfo().getReasonPhrase());
        }

        return response
            .readEntity(User.class);
    }

    public String getId() {
        return id;
    }
}
