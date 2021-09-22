package wappsto.iot.model.schema.network;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.model.schema.*;

import java.util.*;
public class ValueSchema {
    @JsonProperty public String name;
    @JsonProperty public String permission;
    @JsonProperty public List<State> state;
    @JsonProperty public Number number;
    @JsonProperty public Meta meta;

    public ValueSchema(String name, String permission, Number number) {
        this.name = name;
        this.permission = permission;
        state = new LinkedList<>();
        if (permission.contains("r")) state.add(new State("Report"));
        if (permission.contains("w")) state.add(new State("Control"));
        this.number = number;
        meta = new Meta("Value");
    }
}
