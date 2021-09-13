package wappsto.rest.session.core;

import wappsto.rest.model.*;
import wappsto.rest.request.*;
import wappsto.rest.session.model.*;
import wappsto.rest.session.Wapps;

import javax.ws.rs.core.Response;
import java.util.Collection;

public class User extends  Session{

    /**
     * Create an API session with regular user privileges
     * @param credentials   Login credentials
     * @param serviceUrl    URL to API
     * @throws Exception
     */
    public User(
        Credentials credentials,
        String serviceUrl
    ) throws Exception {
        super(credentials, serviceUrl);
    }

    /**
     * Create a session object from an existing session ID
     * @param id            Session ID
     * @param serviceUrl    URL to API
     * @throws Exception
     */
    public User(String id, String serviceUrl) throws Exception {
        super(id, serviceUrl);
        //Ensure the session token is valid
        fetchUser();
    }

    /**
     * Fetch user information associated with the session
     * @return
     * @throws Exception
     */
    public UserResponse fetchUser() throws Exception{
        return new Request.Builder(service)
            .atEndPoint(API.USER)
            .atEndPoint("me")
            .get(id)
            .readEntity(UserResponse.class);
    }

    public static class Builder {
        private final Admin admin;
        private final String serviceUrl;
        private Credentials credentials;

        /**
         * Builder class used to register new users and log them in
         * @param admin
         * @param serviceUrl
         */
        public Builder(Admin admin, String serviceUrl) {
            this.admin = admin;
            this.serviceUrl = serviceUrl;
        }

        /**
         * @param credentials
         * @return
         */
        public Builder withCredentials(Credentials credentials) {
            this.credentials = credentials;
            return this;
        }

        /**
         * Register the user and instantiate the new session
         * @return
         * @throws Exception
         */
        public User create() throws Exception {
            admin.register(credentials);
            return new User(credentials, serviceUrl);
        }

    }
}
