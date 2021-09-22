package wappsto.iot;

import wappsto.iot.model.*;
import wappsto.iot.rpc.*;
import wappsto.iot.model.schema.network.*;

import java.util.*;

public class VirtualIoTNetwork {
    private final NetworkSchema schema;
    private final IoTClient client;
    private HashMap<String, Device> devices;

    public VirtualIoTNetwork(NetworkSchema schema, IoTClient client) {
        this.schema = schema;
        this.client = client;

        devices = new HashMap<>();
        for (DeviceSchema d : schema.device) {
            devices.put(d.name, new Device(d.meta.id, d.value));
        }

        client.publish(schema);
    }

}
