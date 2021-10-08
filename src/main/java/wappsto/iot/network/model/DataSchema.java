package wappsto.iot.network.model;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.model.schema.*;
import wappsto.iot.rpc.model.schema.outgoing.*;

public class DataSchema extends Data {
    @JsonProperty public String name;
    @JsonProperty public Meta meta;

    public DataSchema(String name, String id, String type) {
        this.meta = new Meta(type, id);
        this.name = name;
    }
    public DataSchema() {}
}
