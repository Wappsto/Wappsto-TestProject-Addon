package wappsto.iot.rpc.model.request;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class RPCData {
    @JsonProperty public String name;
    @JsonProperty public Collection<Device> device;
    @JsonProperty public Meta meta;

    public RPCData(String name, Collection<Device> device, String id) {
        this.name = name;
        this.device = device;
        this.meta = new Meta("network", id);
    }
}
