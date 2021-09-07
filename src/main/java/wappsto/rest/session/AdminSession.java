package wappsto.rest.session;
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
            .atEndPoint(API.SESSION)
            .withBody(credentials)
            .post();

        id = response
            .readEntity(SessionResponse.class).sessionId.id;
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
