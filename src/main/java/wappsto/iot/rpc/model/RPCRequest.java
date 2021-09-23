package wappsto.iot.rpc.model;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.*;

public class RPCRequest {
    @JsonProperty public String jsonrpc;
    @JsonProperty public Methods method;
    @JsonProperty public Params params;
    @JsonProperty public String id;

    public RPCRequest(Params params, Methods method) {
        jsonrpc = "2.0";
        this.params = params;
        this.method = method;
        this.id = Utils.requestId(method);
    }
}
