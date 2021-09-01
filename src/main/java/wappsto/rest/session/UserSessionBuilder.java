package wappsto.rest.session;

import wappsto.rest.model.Credentials;

public class UserSessionBuilder {
    private AdminSession admin;

    private Credentials credentials;
    public UserSessionBuilder(AdminSession admin) {
        this.admin = admin;
    }

    public UserSessionBuilder(AdminSession admin, Credentials credentials) {
        this.admin = admin;
        this.credentials = credentials;
    }

    public UserSessionBuilder withCredentials(Credentials credentials) {
        return new UserSessionBuilder(admin, credentials);
    }

    public UserSession create() throws Exception {
        admin.register(credentials);
        return new UserSession(credentials);
    }

}
