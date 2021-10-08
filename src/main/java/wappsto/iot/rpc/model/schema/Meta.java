package wappsto.iot.rpc.model.schema;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class Meta {
    @JsonProperty public UUID id;
    @JsonProperty public String type;
    @JsonProperty public String version;

    public Meta(String type) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.version = "2.0";
    }
    public Meta(String type, String id) {
        this(type);
        this.id = UUID.fromString(id);
    }

    public Meta() {
    }
}
