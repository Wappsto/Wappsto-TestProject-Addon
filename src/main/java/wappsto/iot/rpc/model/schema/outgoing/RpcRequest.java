package wappsto.iot.rpc.model.schema.outgoing;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.model.schema.*;

public class RpcRequest extends RpcBase {
    @JsonProperty public Params params;

    public RpcRequest(Params params, Methods method) {
        jsonrpc = "2.0";
        this.params = params;
        this.method = method;
        this.id = Utils.requestId(method);
    }

}
