package wappsto.api.rest.network;

import wappsto.api.network.*;
import wappsto.api.network.model.*;
import wappsto.api.rest.request.*;
import wappsto.api.rest.session.*;
import wappsto.api.session.model.*;

import java.util.*;

public class RestNetworkService implements NetworkService {
    private final RestSession session;

    public RestNetworkService(RestSession session) {
        this.session = session;
    }

    /**
     * Claim an already existing network by its UUID
     * @param id
     * @throws Exception
     */
    @Override
    public void claim(String id) throws Exception {
        new Request.Builder(session.service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.NETWORK)
            .atEndPoint(id)
            .withBody("{}")
            .post(session.getId());
    }

    /**
     * Make a new network creator - used for creating virtual IoT devices
     * @return network creator
     * @throws Exception
     */
    @Override
    public CreatorResponse getCreator() throws Exception {
        return getCreator(false);
    }

    @Override
    public CreatorResponse getCreator(boolean manufacturerAsOwner)
        throws Exception
    {
        return ((CreatorResponse) new Request.Builder(session.service)
            .atEndPoint(API.V2_1)
            .atEndPoint(API.CREATOR)
            .withBody(new CreatorRequest(manufacturerAsOwner))
            .post(session.getId(), CreatorResponse.class));
    }

    /**
     * Create virtual network. This does not return a network creator
     * @return Network
     * @throws Exception
     */
    @Override
    public NetworkMeta createNetwork() throws Exception {
        NetworkResponse response =(
            (NetworkResponse) new Request.Builder(session.service)
                .atEndPoint(API.V2_0)
                .atEndPoint(API.NETWORK)
                .withBody("{}")
                .post(session.getId(), NetworkResponse.class)
        );

        return response.meta;
    }

    /**
     * Fetch existing network
     * @param id
     * @return Network
     * @throws Exception
     */
    @Override
    public NetworkMeta fetch(String id) throws Exception {
        return ((NetworkResponse) (new Request.Builder(session.service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.NETWORK)
            .atEndPoint(id)
            .get(session.getId(), NetworkResponse.class)))
            .meta;
    }

    /**
     * Share existing network with another user
     * @param network Network
     * @param friend Friend
     * @throws Exception
     */
    @Override
    public void share(NetworkMeta network, UserResponse friend) throws Exception {
        share(network.id, friend.username);
    }

    /**
     * Share existing network with another user
     * @param networkId Network UUID
     * @param friendUsername Friend username
     * @throws Exception
     */
    @Override
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
            .post(session.getId());
    }

    /**
     * Update a control state on a network
     * @param id State UUID
     * @param data Desired value
     * @throws Exception
     */
    @Override
    public void updateState(UUID id, String data) throws Exception {
        new Request.Builder(session.service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.STATE)
            .atEndPoint(id.toString())
            .withBody(new UpdateStateRequest(data))
            .patch(session.getId());
    }

    /**
     * Get the current state of a value of a network.
     * Usually used for getting the current report state of a device value
     * @param id State UUID
     * @return State value
     * @throws Exception
     */
    @Override
    public String getState(UUID id) throws Exception {
        return ((StateResponse) new Request.Builder(session.service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.STATE)
            .atEndPoint(id.toString())
            .get(session.getId(), StateResponse.class))
            .data;



    }

    @Override
    public String createDevice(String networkId) throws Exception {
        return ((CreateDeviceResponse) new Request.Builder(session.service)
            .atEndPoint(API.V2_0)
            .atEndPoint(API.NETWORK)
            .atEndPoint(networkId)
            .atEndPoint(API.DEVICE)
            .withBody("{}")
            .post(session.getId(), CreateDeviceResponse.class)).meta.id;
    }
}
