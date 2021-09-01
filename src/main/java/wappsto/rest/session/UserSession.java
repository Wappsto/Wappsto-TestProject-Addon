package wappsto.rest.session;

import org.glassfish.jersey.client.ClientConfig;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import wappsto.rest.model.*;

public class UserSession  extends  Session{

    public UserSession(Credentials credentials) {
        super();

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
