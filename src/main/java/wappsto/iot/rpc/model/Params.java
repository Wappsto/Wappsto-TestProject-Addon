package wappsto.iot.rpc.model;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.network.model.*;

public class Params {
    @JsonProperty public String url;
    @JsonProperty public Data data;

    public Params(String url, Data data) {
        this.url = url;
        this.data = data;
    }
}
