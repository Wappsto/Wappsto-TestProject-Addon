package wappsto.iot;

import wappsto.iot.network.*;

import java.util.*;

public class ClientStore {
    private static ClientStore instance;
    private VirtualIoTNetwork client;

    private ClientStore() {
    }

    public static ClientStore getInstance() {
        if (instance == null) {
            instance = new ClientStore();
        }

        return instance;
    }

    public void setClient(VirtualIoTNetwork client) {
        this.client = client;
    }

    public VirtualIoTNetwork getClient() {
        return client;
    }
}
