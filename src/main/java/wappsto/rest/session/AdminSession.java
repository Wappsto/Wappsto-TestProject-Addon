package wappsto.rest.session;
import org.glassfish.jersey.client.ClientConfig;
import wappsto.rest.exceptions.Forbidden;
import wappsto.rest.model.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;

public class AdminSession {
    private String sessionId;
    private Client client;
    private WebTarget api;


    public AdminSession(AdminCredentials credentials) throws Forbidden {
        client = ClientBuilder.newClient(new ClientConfig());
        api = client.target("https://qa.wappsto.com/services/2.1/");

        Response response = api.path("session")
            .request("application/json")
            .post(Entity.json(credentials));
        sessionId = response
            .readEntity(SessionResponse.class).sessionId.id;
    }

    public void register(Credentials credentials) {
        api.path("register")
            .request("application/json")
            .header("x-session", sessionId)
            .post(Entity.json(credentials));
    }

    public void delete(String username) {
        api.path("register")
            .path(username)
            .request()
            .header("x-session", sessionId)
            .delete();
    }

    public User fetchUser(String username) {
        Response response = api.path("user")
            .path(username)
            .request()
            .header("x-session", sessionId)
            .get();
        return response
            .readEntity(User.class);

    }

    public String getSessionId() {
        return sessionId;
    }
}
