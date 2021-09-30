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

    /**
     * Make a new network creator - used for creating virtual IoT devices
     * @return network creator
     * @throws Exception
     */
    public CreatorResponse getCreator() throws Exception {
        CreatorResponse response = ((CreatorResponse) new Request.Builder(session.service)
            .atEndPoint(API.V2_1)
            .atEndPoint(API.CREATOR)
            .withBody("{}")
            .post(session.id, CreatorResponse.class));
        claim(response.network.id);
        return response;
    }

    /**
     * Create virtual network. This does not return a network creator
     * @return Network
     * @throws Exception
     */
    public NetworkMeta create() throws Exception {
        return ((NetworkResponse) new Request.Builder(session.service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.NETWORK)
            .withBody("{}")
            .post(session.id, NetworkResponse.class))
            .meta;
    }

    /**
     * Fetch existing network
     * @param id
     * @return Network
     * @throws Exception
     */
    public NetworkMeta fetch(String id) throws Exception {
        return ((NetworkResponse) (new Request.Builder(session.service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.NETWORK)
            .atEndPoint(id)
            .get(session.id, NetworkResponse.class)))
            .meta;
    }

    /**
     * Share existing network with another user
     * @param network Network
     * @param friend Friend
     * @throws Exception
     */
    public void share(NetworkMeta network, UserResponse friend) throws Exception {
        share(network.id, friend.username);
    }

    /**
     * Share existing network with another user
     * @param networkId Network UUID
     * @param friendUsername Friend username
     * @throws Exception
     */
    public void share(String networkId, String friendUsername) throws Exception {
        NetworkShareRequest request = new NetworkShareRequest(
            friendUsername
        );

        new Request.Builder(session.service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.ACL)
            .atEndPoint(networkId)
            .atEndPoint("permission")
            .withBody(request)
            .post(session.id);
    }

    /**
     * Update a control state on a network
     * @param id State UUID
     * @param data Desired value
     * @throws Exception
     */
    public void updateState(UUID id, String data) throws Exception {
        new Request.Builder(session.service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.STATE)
            .atEndPoint(id.toString())
            .withBody(new UpdateStateRequest(data))
            .patch(session.id);
    }

    /**
     * Get the current state of a value of a network.
     * Usually used for getting the current report state of a device value
     * @param id State UUID
     * @return State value
     * @throws Exception
     */
    public String getState(UUID id) throws Exception {
        return ((StateResponse) new Request.Builder(session.service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.STATE)
            .atEndPoint(id.toString())
            .get(session.id, StateResponse.class))
            .data;



    }
}
