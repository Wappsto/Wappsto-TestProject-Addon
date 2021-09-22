package wappsto.iot.rpc.model.schema.network;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.model.schema.*;

public class Data {
    @JsonProperty public Meta meta;
    @JsonProperty public String name;

    public Data(String name, String id, String type) {
        this.name = name;
        this.meta = new Meta(type, id);
    }
}
