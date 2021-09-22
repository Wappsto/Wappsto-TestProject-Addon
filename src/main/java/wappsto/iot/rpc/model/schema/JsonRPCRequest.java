package wappsto.iot.rpc.model.schema;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.model.schema.network.*;

public class JsonRPCRequest {
    @JsonProperty public String jsonrpc;
    @JsonProperty public Methods method;
    @JsonProperty public Network params;
    @JsonProperty public String id;

    public JsonRPCRequest(Network params, Methods method) {
        jsonrpc = "2.0";
        this.params = params;
        this.method = method;
        this.id = Utils.requestId(method);
    }
}
