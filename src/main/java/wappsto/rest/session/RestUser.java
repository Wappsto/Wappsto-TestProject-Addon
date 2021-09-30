package wappsto.rest.session;

import wappsto.rest.request.*;
import wappsto.session.model.*;
import wappsto.session.*;

public class RestUser extends RestSession implements User {

    /**
     * Create an API session with regular user privileges
     * @param credentials   Login credentials
     * @param serviceUrl    URL to API
     * @throws Exception
     */
    public RestUser(
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
    public RestUser(String id, String serviceUrl) throws Exception {
        super(id, serviceUrl);
        //Ensure the session token is valid
        fetchMe();
    }

    /**
     * Fetch user information associated with the session
     * @return user
     * @throws Exception
     */
    @Override
    public UserResponse fetchMe() throws Exception{
        return (UserResponse) new Request.Builder(service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.USER)
            .atEndPoint("me")
            .get(id, UserResponse.class);
    }

    /**
     *
     * @return session id
     */
    @Override
    public String getId() {
        return id;
    }

    public static class Builder implements UserBuilder {
        private final RestAdmin admin;
        private final String serviceUrl;
        private Credentials credentials;

        /**
         * Builder class used to register new users and log them in
         * @param admin Admin session
         * @param serviceUrl API URL
         */
        public Builder(RestAdmin admin, String serviceUrl) {
            this.admin = admin;
            this.serviceUrl = serviceUrl;
        }

        /**
         * @param credentials
         * @return this
         */
        @Override
        public Builder withCredentials(Credentials credentials) {
            this.credentials = credentials;
            return this;
        }

        /**
         * Register the user and instantiate the new session
         * @return this
         * @throws Exception
         */
        @Override
        public RestUser create() throws Exception {
            admin.register(credentials);
            return new RestUser(credentials, serviceUrl);
        }

    }
}
