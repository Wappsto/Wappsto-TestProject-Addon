package wappsto.rest.session;

import wappsto.rest.model.*;
import wappsto.rest.request.*;
import wappsto.rest.session.model.*;

import javax.ws.rs.core.Response;
import java.util.Collection;

public class User extends  Session{

    public User(
        Credentials credentials,
        String serviceUrl
    ) throws Exception {
        super(credentials, serviceUrl);
    }

    public User(String id, String serviceUrl) throws Exception {
        super(id, serviceUrl);
        //Ensure the session token is valid
        fetchUser();
    }

    public UserResponse fetchUser() throws Exception{
        return new Request.Builder(service)
            .atEndPoint(API.USER)
            .atEndPoint("me")
            .get(id)
            .readEntity(UserResponse.class);
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

    public void install(String nameOfWapp) throws Exception {
        install(Wapp.from(nameOfWapp));
    }

    public Collection<String> fetchWapps() throws Exception {
        Response response = new Request.Builder(service)
            .atEndPoint(API.INSTALLATION)
            .get(id);

        return response.readEntity(WappsResponse.class).id;
    }

    public void claimNetwork(String id) throws Exception {
        new Request.Builder(service)
            .atEndPoint(API.NETWORK)
            .atEndPoint(id)
            .withBody("{}")
            .post(this.id);
    }

    public static class Builder {
        private final Admin admin;
        private final String serviceUrl;

        private Credentials credentials;
        public Builder(Admin admin, String serviceUrl) {
            this.admin = admin;
            this.serviceUrl = serviceUrl;
        }

        public Builder withCredentials(Credentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public User create() throws Exception {
            admin.register(credentials);
            return new User(credentials, serviceUrl);
        }

    }
}
