package wappsto.rest.network;

import wappsto.rest.network.model.*;
import wappsto.rest.request.*;
import wappsto.rest.session.*;
import wappsto.rest.session.model.*;

public class NetworkService {
    private Session session;

    public NetworkService(Session session) {
        this.session = session;
    }

    /**
     * Claim an already existing network by its UUID
     * @param id
     * @throws Exception
     */
    public void claim(String id) throws Exception {
        new Request.Builder(session.service)
            .atEndPoint(API.NETWORK)
            .atEndPoint(id)
            .withBody("{}")
            .post(session.id);
    }

    public CreatorResponse getCreator() throws Exception {
        return new Request.Builder(session.service)
            .atEndPoint(API.V2_1)
            .atEndPoint(API.CREATOR)
            .withBody("{}")
            .post(session.id)
            .readEntity(CreatorResponse.class);
    }

    public NetworkMeta create() throws Exception {
        return new Request.Builder(session.service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.NETWORK)
            .withBody("{}")
            .post(session.id)
            .readEntity(NetworkResponse.class)
            .meta;
    }

    public NetworkMeta fetch(String id) throws Exception {
        return new Request.Builder(session.service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.NETWORK)
            .atEndPoint(id)
            .get(session.id)
            .readEntity(NetworkResponse.class)
            .meta;
    }

    public void share(NetworkMeta network, UserResponse friend) throws Exception {
        NetworkShareRequest request = new NetworkShareRequest(
            friend.meta.id
        );

        new Request.Builder(session.service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.ACL)
            .atEndPoint(network.id)
            .atEndPoint("permission")
            .withBody(request)
            .post(session.id);
    }
}
