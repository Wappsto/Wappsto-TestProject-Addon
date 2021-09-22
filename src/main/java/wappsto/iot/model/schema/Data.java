package wappsto.iot.model.schema;

import com.fasterxml.jackson.annotation.*;

public class Data {
    @JsonProperty public Meta meta;
    @JsonProperty public String name;

    public Data(String name, String id, String type) {
        this.name = name;
        this.meta = new Meta(type, id);
    }
}
