package wappsto.iot.rpc.model;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.*;

public class RpcRequest extends RpcBase {
    @JsonProperty public Params params;

    public RpcRequest(Params params, Methods method) {
        jsonrpc = "2.0";
        this.params = params;
        this.method = method;
        this.id = Utils.requestId(method);
    }

}
