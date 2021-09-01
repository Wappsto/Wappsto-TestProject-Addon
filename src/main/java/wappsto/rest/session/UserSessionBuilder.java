package wappsto.rest.session;

import wappsto.rest.model.Credentials;

public class UserSessionBuilder {
    private AdminSession admin;
    private String serviceUrl;

    private Credentials credentials;
    public UserSessionBuilder(AdminSession admin, String serviceUrl) {
        this.admin = admin;
        this.serviceUrl = serviceUrl;
    }

    private UserSessionBuilder(AdminSession admin, String serviceUrl, Credentials credentials) {
        this(admin, serviceUrl);
        this.credentials = credentials;
    }

    public UserSessionBuilder withCredentials(Credentials credentials) {
        return new UserSessionBuilder(admin, serviceUrl, credentials);
    }

    public UserSession create() throws Exception {
        admin.register(credentials);
        return new UserSession(credentials, serviceUrl);
    }

}
