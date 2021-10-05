package wappsto.iot;

import wappsto.iot.network.*;

import java.util.*;

public class VirtualIoTNetworkStore {
    private static VirtualIoTNetworkStore instance;
    private static HashMap<UUID, VirtualIoTNetwork> networks;

    private VirtualIoTNetworkStore() {
        networks = new HashMap<>();
    }

    public static VirtualIoTNetworkStore getInstance() {
        if (instance == null) {
            instance = new VirtualIoTNetworkStore();
        }

        return instance;
    }

    public void addNetwork(VirtualIoTNetwork network) {
        networks.put(network.schema.meta.id, network);
    }

    public VirtualIoTNetwork getBy(UUID id) {
        return networks.get(id);
    }

    public void stopAll() {
        networks.forEach((name, network) -> {
            network.stop();
            networks.remove(name);
        });
    }

    public void removeBy(UUID id) {
        networks.remove(id);
    }

}
