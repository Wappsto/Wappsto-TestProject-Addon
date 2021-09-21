package wappsto.iot.rpc.model.request;

import com.fasterxml.jackson.annotation.*;
import java.util.*;
public class Value {
    @JsonProperty public String name;
    @JsonProperty public String permission;
    @JsonProperty public Collection<State> state;
    @JsonProperty public Number number;
    @JsonProperty public Meta meta;

    public Value(String name, String permission, Number number) {
        this.name = name;
        this.permission = permission;
        state = new LinkedList<>();
        if (permission.contains("r")) state.add(new State("Report"));
        if (permission.contains("w")) state.add(new State("Control"));
        this.number = number;
        meta = new Meta("Value");
    }
}
