package extensions.mocks;

import wappsto.api.network.*;
import wappsto.api.network.model.*;
import wappsto.api.session.model.*;

import java.util.*;

public class InMemoryNetworkService implements NetworkService {
    private final HashMap<UUID, String> states;
    private final HashMap<UUID, NetworkMeta> networks;

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
    public CreatorResponse getCreator(boolean manufacturerAsOwner) throws Exception {
        return null;
    }

    @Override
    public NetworkMeta createNetwork() throws Exception {
        NetworkMeta meta = new NetworkMeta();
        UUID id = UUID.randomUUID();
        meta.id = id.toString();
        meta.device = new LinkedList<>();
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

    @Override
    public String createDevice(String networkId) throws Exception {
        UUID id = UUID.randomUUID();
        try {
            networks.get(UUID.fromString(networkId)).device.add(id.toString());
        } catch (NullPointerException e) {
            throw new Exception("Network not found");
        }
        return id.toString();
    }

    @Override
    public DeviceResponse getDevice(UUID device) {
        return null;
    }

    @Override
    public ValueResponse getValue(UUID fromString) throws Exception {
        return null;
    }
}
