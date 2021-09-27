package wappsto.rest.session;

import org.glassfish.jersey.client.*;
import wappsto.rest.request.Request;
import wappsto.rest.request.*;
import wappsto.rest.session.model.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

public abstract class Session {
    public final String id;
    protected Client client;
    public final WebTarget service;

    /**
     * The base class for API sessions
     * @param credentials   Login credentials
     * @param serviceUrl    URL to the API
     * @throws Exception
     */
    public Session(Credentials credentials, String serviceUrl) throws Exception {
        createClient(serviceUrl);
        service = client.target(serviceUrl);

        id = new Request.Builder(service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.SESSION)
            .withBody(credentials)
            .post()
            .readEntity(SessionResponse.class).sessionMeta.id;
    }

    /**
     * Instantiates a session object from an existing session ID
     * @param id            Session ID
     * @param serviceUrl    URL to the API
     */
    public Session(String id, String serviceUrl) {
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
    public UserResponse fetchUser(String username) throws Exception {
        Response response = new Request.Builder(service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.USER)
            .atEndPoint(username)
            .get(id);
        return response
            .readEntity(UserResponse.class);
    }
}
