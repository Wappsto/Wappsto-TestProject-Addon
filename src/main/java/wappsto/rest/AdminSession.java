package wappsto.rest;
import org.glassfish.jersey.client.ClientConfig;
import wappsto.rest.model.Credentials;
import wappsto.rest.model.SessionResponse;
import wappsto.rest.model.User;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;

public class AdminSession {
    private String sessionId;
    private Client client;
    private WebTarget api;


    public AdminSession(Credentials credentials) {
        client = ClientBuilder.newClient(new ClientConfig());
        api = client.target("https://qa.wappsto.com/services/2.1/");

        Response response = api.path("session")
            .request("application/json")
            .post(Entity.json(credentials));
        sessionId = response
            .readEntity(SessionResponse.class).sessionId.id;
    }

    public UserSession register(Credentials credentials) {
        Response response = api.path("register")
            .request("application/json")
            .header("X-Session", sessionId)
            .post(Entity.json(credentials));

        return new UserSession(response
            .readEntity(SessionResponse.class).sessionId.id);
    }

    public void delete(String username) {

    }
}
