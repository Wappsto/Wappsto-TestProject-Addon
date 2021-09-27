package wappsto.iot.rpc.model;

import com.fasterxml.jackson.annotation.*;

public class SuccessResponseToServer {
    @JsonProperty public String jsonrpc;
    @JsonProperty public String id;
    @JsonProperty public SuccessResponseResult result;

    public SuccessResponseToServer(String id) {
        jsonrpc = "2.0";
        this.id = id;
        result = new SuccessResponseResult(true);
    }
}
