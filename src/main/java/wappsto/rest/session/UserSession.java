package wappsto.rest.session;

import wappsto.rest.model.*;
import wappsto.rest.request.API;
import wappsto.rest.request.Request;

public class UserSession  extends  Session{

    public UserSession(Credentials credentials, String serviceUrl) throws Exception {
        super(serviceUrl);

        id = new Request.Builder(service)
            .atEndPoint(API.SESSION)
            .withBody(credentials)
            .post()
            .readEntity(SessionResponse.class).sessionId.id;
    }

    public User fetchUser() throws Exception{
        return new Request.Builder(service)
            .atEndPoint(API.USER)
            .withSession(id)
            .get("me")
            .readEntity(User.class);
    }

    public String getId() {
        return id;
    }
}
