package wappsto.rest.session;

import wappsto.rest.request.*;
import wappsto.rest.session.model.*;

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
     * @return user
     * @throws Exception
     */
    public UserResponse fetchUser() throws Exception{
        return (UserResponse) new Request.Builder(service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.USER)
            .atEndPoint("me")
            .get(id, UserResponse.class);
    }

    public static class Builder {
        private final Admin admin;
        private final String serviceUrl;
        private Credentials credentials;

        /**
         * Builder class used to register new users and log them in
         * @param admin Admin session
         * @param serviceUrl API URL
         */
        public Builder(Admin admin, String serviceUrl) {
            this.admin = admin;
            this.serviceUrl = serviceUrl;
        }

        /**
         * @param credentials
         * @return this
         */
        public Builder withCredentials(Credentials credentials) {
            this.credentials = credentials;
            return this;
        }

        /**
         * Register the user and instantiate the new session
         * @return this
         * @throws Exception
         */
        public User create() throws Exception {
            admin.register(credentials);
            return new User(credentials, serviceUrl);
        }

    }
}
