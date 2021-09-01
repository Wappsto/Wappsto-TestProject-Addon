package wappsto.rest.session;
import org.glassfish.jersey.client.ClientConfig;
import wappsto.rest.exceptions.Forbidden;
import wappsto.rest.model.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;

public class AdminSession extends Session{


    public AdminSession(AdminCredentials credentials) throws Forbidden {
        super();

        Response response = api.path("session")
            .request("application/json")
            .post(Entity.json(credentials));
        id = response
            .readEntity(SessionResponse.class).sessionId.id;
    }

    public void register(Credentials credentials) {
        api.path("register")
            .request("application/json")
            .header("x-session", id)
            .post(Entity.json(credentials));
    }

    public void delete(String username) {
        api.path("register")
            .path(username)
            .request()
            .header("x-session", id)
            .delete();
    }

    public User fetchUser(String username) {
        Response response = api.path("user")
            .path(username)
            .request()
            .header("x-session", id)
            .get();
        return response
            .readEntity(User.class);

    }

    public String getId() {
        return id;
    }
}
