package wappsto.iot.network.model;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.model.schema.*;

import java.util.*;

public class DeviceSchema {
    @JsonProperty public String name;
    @JsonProperty public List<ValueSchema> value;
    @JsonProperty public Meta meta;

    public DeviceSchema(String name, List<ValueSchema> value) {
        this.name = name;
        this.value = value;
        meta = new Meta("Device");
    }

    public DeviceSchema(String name) {
        this(name, new LinkedList<>());
    }

    public DeviceSchema() {}

    public static class Builder {
        private final NetworkSchema.Builder network;
        private final String name;
        private final List<ValueSchema> values;

        public Builder(String name, NetworkSchema.Builder network) {
            this.name = name;
            this.network = network;
            values = new LinkedList<>();
        }

        public ValueSchema.Builder addValue(
            String name,
            ValuePermission permission
        ) {
            return new ValueSchema.Builder(name, permission, this);
        }

        void add(ValueSchema valueSchema) {
            values.add(valueSchema);
        }

        public NetworkSchema.Builder addToNetwork() {
            network.add(new DeviceSchema(name, values));
            return network;
        }
    }
}
