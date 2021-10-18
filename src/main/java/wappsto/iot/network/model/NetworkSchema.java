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
}
