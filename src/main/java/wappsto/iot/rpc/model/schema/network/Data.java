package wappsto.iot.rpc.model.schema.network;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.model.schema.*;

import java.util.*;

public class Data {
    @JsonProperty public String name;
    @JsonProperty public Collection<Device> device;
    @JsonProperty public Meta meta;

    public Data(String name, Collection<Device> device, String id) {
        this.name = name;
        this.device = device;
        this.meta = new Meta("network", id);
    }
}
