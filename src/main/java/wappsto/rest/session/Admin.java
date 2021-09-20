package wappsto.rest.session;

import wappsto.rest.request.Request;
import wappsto.rest.request.*;
import wappsto.rest.session.model.*;

import javax.ws.rs.core.*;

public class Admin extends Session{

    /**
     * An API session with admin privileges
     * @param credentials   Admin username and password used to log in and
     *                      retrieve the session
     * @param serviceUrl    URL to the API
     * @throws Exception    Throws an HttpException if admin credentials or API
     *                      URL are invalid
     */

    public Admin(
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
    public void register(Credentials credentials) throws Exception {
        new Request.Builder(service)
            .withBody(credentials)
            .atEndPoint(API.REGISTER)
            .post(id);
    }

    /**
     * Delete an existing user by username
     * @param username
     * @throws Exception
     */
    public void delete(String username) throws Exception {
        new Request.Builder(service)
            .atEndPoint(API.REGISTER)
            .atEndPoint(username)
            .delete(id);
    }

    /**
     * Fetch a user by username. Currently, only the username gets deserialized
     * from the server response, so this method is only useful for validating
     * the correctness of other methods.
     * @param username
     * @return
     * @throws Exception
     */
    public UserResponse fetchUser(String username) throws Exception {
        Response response = new Request.Builder(service)
            .atEndPoint(API.USER)
            .atEndPoint(username)
            .get(id);
        return response
            .readEntity(UserResponse.class);
    }
}
