package wappsto.iot.rpc.model.schema.network;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.model.schema.*;

import java.text.*;
import java.util.*;

public class State {
    @JsonProperty public String data;
    @JsonProperty public String type;
    @JsonProperty public String timestamp;
    @JsonProperty public Meta meta;

    public State(String type) {
        data = "";
        this.type = type;
        timestamp = new SimpleDateFormat().format(new Date());
        meta = new Meta("State");
    }
}
