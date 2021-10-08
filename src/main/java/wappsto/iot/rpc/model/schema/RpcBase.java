package wappsto.iot.rpc.model.schema;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RpcBase {
    @JsonProperty
    public String jsonrpc;
    @JsonProperty
    public Methods method;
    @JsonProperty
    public String id;
}
