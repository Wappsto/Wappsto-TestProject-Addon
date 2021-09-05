package wappsto.rest.session;

import wappsto.rest.model.*;
import wappsto.rest.request.API;
import wappsto.rest.request.Request;

public class UserSession  extends  Session{

    public UserSession(
        Credentials credentials,
        String serviceUrl
    ) throws Exception {
        super(serviceUrl);

        id = new Request.Builder(service)
            .addPath(API.SESSION)
            .withBody(credentials)
            .post()
            .readEntity(SessionResponse.class).sessionId.id;
    }

    public User fetchUser() throws Exception{
        return new Request.Builder(service)
            .addPath(API.USER)
            .addPath("me")
            .get(id)
            .readEntity(User.class);
    }

    public String getId() {
        return id;
    }

    public static class Builder {
        private AdminSession admin;
        private String serviceUrl;

        private Credentials credentials;
        public Builder(AdminSession admin, String serviceUrl) {
            this.admin = admin;
            this.serviceUrl = serviceUrl;
        }

        public Builder withCredentials(Credentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public UserSession create() throws Exception {
            admin.register(credentials);
            return new UserSession(credentials, serviceUrl);
        }

    }
}
