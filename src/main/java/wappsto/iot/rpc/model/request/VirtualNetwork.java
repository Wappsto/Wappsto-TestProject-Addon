package wappsto.iot.rpc.model.request;

import com.fasterxml.jackson.annotation.*;

public class VirtualNetwork {
    @JsonProperty public String jsonrpc = "2.0";
    @JsonProperty public String method;
    @JsonProperty public Params params;
    @JsonProperty public String id;

    public VirtualNetwork(Params params, String method, String id) {
        this.params = params;
        this.method = method;
        this.id = id;
    }
}
