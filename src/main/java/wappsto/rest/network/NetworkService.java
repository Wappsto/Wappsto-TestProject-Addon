package wappsto.rest.network;

import wappsto.rest.network.model.*;
import wappsto.rest.request.*;
import wappsto.rest.session.*;
import wappsto.rest.session.model.*;

import java.util.*;

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
            .atEndPoint(API.V2_0)
            .atEndPoint(API.NETWORK)
            .atEndPoint(id)
            .withBody("{}")
            .post(session.id);
    }

    public CreatorResponse getCreator() throws Exception {
        CreatorResponse response = new Request.Builder(session.service)
            .atEndPoint(API.V2_1)
            .atEndPoint(API.CREATOR)
            .withBody("{}")
            .post(session.id)
            .readEntity(CreatorResponse.class);
        claim(response.network.id);
        return response;
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
        share(network.id, friend.meta.id);
    }

    public void share(String networkId, String friendId) throws Exception {
        NetworkShareRequest request = new NetworkShareRequest(
            friendId
        );

        new Request.Builder(session.service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.ACL)
            .atEndPoint(networkId)
            .atEndPoint("permission")
            .withBody(request)
            .post(session.id);
    }

    public void updateState(UUID id, String data) throws Exception {
        new Request.Builder(session.service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.STATE)
            .atEndPoint(id.toString())
            .withBody(new UpdateStateRequest(data))
            .patch(session.id);
    }

    public String getState(UUID id) throws Exception {
        return new Request.Builder(session.service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.STATE)
            .atEndPoint(id.toString())
            .get(session.id)
            .readEntity(StateResponse.class).data;
    }
}
