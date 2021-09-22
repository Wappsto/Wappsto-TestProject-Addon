package wappsto.iot.rpc.model.schema.network;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class Network extends Data{
    @JsonProperty public Collection<Device> device;
    public Network(String name, String id, Collection<Device> device) {
        super(name, id, "network");
        this.device = device;
    }
}
