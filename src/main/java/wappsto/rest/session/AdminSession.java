package wappsto.rest.session;
import wappsto.rest.exceptions.Forbidden;
import wappsto.rest.exceptions.MissingField;
import wappsto.rest.exceptions.NotFound;
import wappsto.rest.model.*;
import wappsto.rest.request.API;
import wappsto.rest.request.Request;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;

public class AdminSession extends Session{


    public AdminSession(AdminCredentials credentials) throws Exception {
        super();
        Response response = new Request(service)
            .atEndPoint(API.SESSION)
            .withBody(credentials)
            .post();

        id = response
            .readEntity(SessionResponse.class).sessionId.id;
    }

    public void register(Credentials credentials) throws Exception {
        new Request(service)
            .withSession(id)
            .withBody(credentials)
            .atEndPoint(API.REGISTER)
            .post();
    }

    public void delete(String username) throws Exception {
        new Request(service)
            .withSession(id)
            .atEndPoint(API.REGISTER)
            .delete(username);
    }

    public User fetchUser(String username) throws Forbidden, NotFound {

        Response response = new Request(service)
            .withSession(id)
            .atEndPoint(API.USER)
            .get(username);
        return response
            .readEntity(User.class);
    }

    public String getId() {
        return id;
    }
}
