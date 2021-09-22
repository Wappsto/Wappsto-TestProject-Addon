package wappsto.iot.model.schema.network;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.model.schema.*;

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
}
