package wappsto.iot.rpc.model.request;

import com.fasterxml.jackson.annotation.*;

public class Params {
    @JsonProperty public String url;
    @JsonProperty public RPCData data;

    public Params(String url, RPCData data) {
        this.url = url;
        this.data = data;
    }
}
