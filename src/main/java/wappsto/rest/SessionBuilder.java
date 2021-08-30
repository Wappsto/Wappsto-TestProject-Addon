package wappsto.rest;

import wappsto.rest.model.Credentials;

public class SessionBuilder {
    private AdminSession admin;

    private Credentials credentials;
    public SessionBuilder(AdminSession admin) {
        this.admin = admin;
    }

    public SessionBuilder(AdminSession admin, Credentials credentials) {
        this.admin = admin;
        this.credentials = credentials;
    }

    public SessionBuilder withCredentials(Credentials credentials) {
        return new SessionBuilder(admin, credentials);
    }

    public UserSession create() {
        return admin.register(credentials);
    }

}
