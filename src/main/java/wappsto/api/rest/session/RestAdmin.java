package wappsto.api.rest.session;

import wappsto.api.rest.request.*;
import wappsto.api.session.*;
import wappsto.api.session.model.*;

public class RestAdmin extends RestSession implements Admin {

    /**
     * An API session with admin privileges
     * @param credentials   Admin username and password used to log in and
     *                      retrieve the session
     * @param serviceUrl    URL to the API
     * @throws Exception    Throws an HttpException if admin credentials or API
     *                      URL are invalid
     */

    public RestAdmin(
        AdminCredentials credentials,
        String serviceUrl
    ) throws Exception {
        super(credentials, serviceUrl);
    }

    /**
     * Register a new user with the given credentials
     * @param credentials
     * @throws Exception
     */
    @Override
    public void register(Credentials credentials) throws Exception {
        new Request.Builder(service)
            .withBody(credentials)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.REGISTER)
            .post(id);
    }

    /**
     * Delete an existing user by username
     * @param username
     * @throws Exception
     */
    @Override
    public void delete(String username) throws Exception {
        new Request.Builder(service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.REGISTER)
            .atEndPoint(username)
            .delete(id);
    }

    /**
     * Get the session ID
     * @return session ID
     */
    @Override
    public String getId() {
        return id;
    }
}
