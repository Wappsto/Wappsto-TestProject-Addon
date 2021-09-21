package wappsto.iot.rpc.model.request;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class Device {
    @JsonProperty public String name;
    @JsonProperty public Collection<Value> value;
    @JsonProperty public Meta meta;

    public Device(String name, Collection<Value> value) {
        this.name = name;
        this.value = value;
        meta = new Meta("Device");
    }
}
