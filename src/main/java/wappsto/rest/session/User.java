package wappsto.rest.session;

import wappsto.rest.model.*;
import wappsto.rest.request.*;
import wappsto.rest.model.InstallationRequest;
import wappsto.rest.session.model.Credentials;

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
        super(serviceUrl);
        this.id = id;
        //Ensure the session token is valid
        fetchUser();
    }

    public wappsto.rest.session.model.User fetchUser() throws Exception{
        return new Request.Builder(service)
            .atEndPoint(API.USER)
            .atEndPoint("me")
            .get(id)
            .readEntity(wappsto.rest.session.model.User.class);
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
        private Admin admin;
        private String serviceUrl;

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
