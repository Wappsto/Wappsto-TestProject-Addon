package wappsto.api.rest.session;

import org.glassfish.jersey.client.*;
import wappsto.api.rest.request.*;
import wappsto.api.session.*;
import wappsto.api.session.model.*;

import javax.ws.rs.client.*;

public abstract class RestSession implements Session {
    protected final String id;
    protected Client client;
    public final WebTarget service;

    /**
     * The base class for API sessions
     * @param credentials   Login credentials
     * @param serviceUrl    URL to the API
     * @throws Exception
     */
    public RestSession(Credentials credentials, String serviceUrl) throws Exception {
        createClient(serviceUrl);
        service = client.target(serviceUrl);

        id = ((SessionResponse) new Request.Builder(service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.SESSION)
            .withBody(credentials)
            .post(SessionResponse.class)).sessionMeta.id;
    }

    /**
     * Instantiates a session object from an existing session ID
     * @param id            Session ID
     * @param serviceUrl    URL to the API
     */
    public RestSession(String id, String serviceUrl) {
        createClient(serviceUrl);
        service = client.target(serviceUrl);
        this.id = id;
    }

    protected void createClient(String serviceUrl) {
        client = ClientBuilder.newClient(new ClientConfig());
    }

    /**
     * Fetch a user by username. Currently, only the username gets deserialized
     * from the server response, so this method is only useful for validating
     * the correctness of other methods.
     * @param username
     * @return
     * @throws Exception
     */
    @Override
    public UserResponse fetchUser(String username) throws Exception {
        return (UserResponse) new Request.Builder(service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.USER)
            .atEndPoint(username)
            .get(id, UserResponse.class);
    }
}
