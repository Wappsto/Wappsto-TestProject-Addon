package wappsto.rest.session;

import org.glassfish.jersey.client.ClientConfig;
import wappsto.rest.model.Credentials;
import wappsto.rest.model.SessionResponse;
import wappsto.rest.model.User;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;

public class UserSession {
    private String id;
    private Client client;
    private WebTarget api;

    public UserSession(Credentials credentials) {
        client = ClientBuilder.newClient(new ClientConfig());
        api = client.target("https://qa.wappsto.com/services/2.1/");

        id = api.path("session")
            .request("application/json")
            .post(Entity.json(credentials))
            .readEntity(SessionResponse.class).sessionId.id;
    }

    public User fetchUser() throws Exception{

        Response response = api.path("user/me")
            .request("application/json")
            .header("x-session", id)
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
