package wappsto.rest.session;
import wappsto.rest.exceptions.*;
import wappsto.rest.model.*;
import wappsto.rest.request.*;
import javax.ws.rs.core.Response;

public class AdminSession extends Session{


    public AdminSession(
        AdminCredentials credentials,
        String serviceUrl
    ) throws Exception {

        super(serviceUrl);
        Response response = new Request.Builder(service)
            .addPath(API.SESSION.toString())
            .withBody(credentials)
            .post();

        id = response
            .readEntity(SessionResponse.class).sessionId.id;
    }

    public void register(Credentials credentials) throws Exception {
        new Request.Builder(service)
            .withBody(credentials)
            .addPath(API.REGISTER.toString())
            .post(id);
    }

    public void delete(String username) throws Exception {
        new Request.Builder(service)
            .addPath(API.REGISTER.toString())
            .addPath(username)
            .delete(id);
    }

    public User fetchUser(String username) throws Exception {
        Response response = new Request.Builder(service)
            .addPath(API.USER.toString())
            .addPath(username)
            .get(id);
        return response
            .readEntity(User.class);
    }

    public String getId() {
        return id;
    }
}
