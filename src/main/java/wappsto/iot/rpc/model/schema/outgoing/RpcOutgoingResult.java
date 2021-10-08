package wappsto.iot.rpc.model.schema.outgoing;

import com.fasterxml.jackson.annotation.*;

public class RpcOutgoingResult {
    @JsonProperty public String jsonrpc;
    @JsonProperty public String id;
    @JsonProperty public SuccessResponseResult result;

    public RpcOutgoingResult(String id) {
        jsonrpc = "2.0";
        this.id = id;
        result = new SuccessResponseResult(true);
    }
}
