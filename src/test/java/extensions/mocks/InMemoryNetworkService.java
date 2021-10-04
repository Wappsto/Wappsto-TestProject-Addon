package extensions.mocks;

import wappsto.network.*;
import wappsto.network.model.*;
import wappsto.session.model.*;

import java.util.*;

public class InMemoryNetworkService implements NetworkService {
    private HashMap<UUID, String> states;
    private HashMap<UUID, NetworkMeta> networks;

    public InMemoryNetworkService() {
        states = new HashMap<>();
        networks = new HashMap<>();
    }

    @Override
    public void claim(String id) throws Exception {
        if (networks.get(UUID.fromString(id)) == null) {
            throw new Exception("Network not found");
        }
    }

    @Override
    public CreatorResponse getCreator() throws Exception {
        return null;
    }

    @Override
    public NetworkMeta create() throws Exception {
        NetworkMeta meta = new NetworkMeta();
        UUID id = UUID.randomUUID();
        meta.id = id.toString();
        networks.put(id, meta);
        return meta;
    }

    @Override
    public NetworkMeta fetch(String id) throws Exception {
        return null;
    }

    @Override
    public void share(NetworkMeta network, UserResponse friend) throws Exception {

    }

    @Override
    public void share(String networkId, String friendUsername) throws Exception {
        if (networks.get(UUID.fromString(networkId)) == null) {
            throw new Exception("Network not found");
        }
    }

    @Override
    public void updateState(UUID id, String data) throws Exception {
        states.put(id, data);
    }

    @Override
    public String getState(UUID id) throws Exception {
        String value = states.get(id);
        if (value == null) throw new Exception("Not found");
        else return value;
    }
}
