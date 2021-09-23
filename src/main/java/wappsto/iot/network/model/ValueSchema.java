package wappsto.iot.network.model;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.model.*;

import java.util.*;
public class ValueSchema {
    @JsonProperty public String name;
    @JsonProperty public String permission;
    @JsonProperty public List<StateSchema> state;
    @JsonProperty public NumberSchema number;
    @JsonProperty public Meta meta;

    public ValueSchema(String name, String permission, NumberSchema number) {
        this.name = name;
        this.permission = permission;
        state = new LinkedList<>();
        if (permission.contains("r")) state.add(new StateSchema("Report"));
        if (permission.contains("w")) state.add(new StateSchema("Control"));
        this.number = number;
        meta = new Meta("Value");
    }
}
