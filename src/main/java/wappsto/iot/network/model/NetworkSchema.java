package wappsto.iot.network.model;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class NetworkSchema extends DataSchema {
    @JsonProperty public List<DeviceSchema> device;
    public NetworkSchema(String name, String id, List<DeviceSchema> device) {
        super(name, id, "network");
        this.device = device;
    }

    public NetworkSchema(String name, String id) {
        super(name, id, "network");
        device = new ArrayList<>();
    }

    public NetworkSchema(){}

    public static class Builder {
        private final String name;
        private final String networkId;
        private final List<DeviceSchema> devices;

        public Builder(String name, String networkId) {
            this.name = name;
            this.networkId = networkId;
            devices = new LinkedList<>();
        }

        public DeviceSchema.Builder addDevice(String name) {
            return new DeviceSchema.Builder(name, this);
        }

        public void add(DeviceSchema deviceSchema) {
            devices.add(deviceSchema);
        }

        public NetworkSchema build() {
            return new NetworkSchema(name, networkId, devices);
        }
    }
}
