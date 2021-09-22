package wappsto.iot.rpc.model.schema.network;

import com.fasterxml.jackson.annotation.*;

public class Params {
    @JsonProperty public String url;
    @JsonProperty public Network data;

    public Params(String url, Network data) {
        this.url = url;
        this.data = data;
    }
}
