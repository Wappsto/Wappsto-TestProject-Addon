package wappsto.iot.model.schema.network;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.model.schema.*;

import java.util.*;

public class NetworkSchema extends Data {
    @JsonProperty public List<DeviceSchema> device;
    public NetworkSchema(String name, String id, List<DeviceSchema> device) {
        super(name, id, "network");
        this.device = device;
    }
}
