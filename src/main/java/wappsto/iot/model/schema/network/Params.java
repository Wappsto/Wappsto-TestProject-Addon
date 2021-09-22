package wappsto.iot.model.schema.network;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.model.schema.*;

public class Params {
    @JsonProperty public String url;
    @JsonProperty public Data data;

    public Params(String url, Data data) {
        this.url = url;
        this.data = data;
    }
}
