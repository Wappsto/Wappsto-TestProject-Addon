package wappsto.rest.session;

import wappsto.rest.model.*;
import wappsto.rest.request.API;
import wappsto.rest.model.InstallationRequest;
import wappsto.rest.request.Request;

import javax.ws.rs.core.Response;
import java.util.Collection;

public class UserSession  extends  Session{

    public UserSession(
        Credentials credentials,
        String serviceUrl
    ) throws Exception {
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
            .atEndPoint("me")
            .get(id)
            .readEntity(User.class);
    }

    public String getId() {
        return id;
    }

    public void install(Wapp wapp) throws Exception {
        InstallationRequest install = new InstallationRequest(
            wapp.id
        );

        new Request.Builder(service)
            .atEndPoint(API.INSTALLATION)
            .withBody(install)
            .post(id);
    }

    public Collection<String> fetchWapps() throws Exception {
        Response response = new Request.Builder(service)
            .atEndPoint(API.INSTALLATION)
            .get(id);

        return response.readEntity(WappsResponse.class).id;
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
