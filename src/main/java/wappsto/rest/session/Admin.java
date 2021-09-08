package wappsto.rest.session;
import wappsto.rest.request.*;
import wappsto.rest.session.model.AdminCredentials;
import wappsto.rest.session.model.Credentials;
import wappsto.rest.session.model.SessionResponse;
import wappsto.rest.session.model.User;

import javax.ws.rs.core.Response;

public class Admin extends Session{


    public Admin(
        AdminCredentials credentials,
        String serviceUrl
    ) throws Exception {
        super(credentials, serviceUrl);
    }

    public void register(Credentials credentials) throws Exception {
        new Request.Builder(service)
            .withBody(credentials)
            .atEndPoint(API.REGISTER)
            .post(id);
    }

    public void delete(String username) throws Exception {
        new Request.Builder(service)
            .atEndPoint(API.REGISTER)
            .atEndPoint(username)
            .delete(id);
    }

    public User fetchUser(String username) throws Exception {
        Response response = new Request.Builder(service)
            .atEndPoint(API.USER)
            .atEndPoint(username)
            .get(id);
        return response
            .readEntity(User.class);
    }

    public String getId() {
        return id;
    }
}
