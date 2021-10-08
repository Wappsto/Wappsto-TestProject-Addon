package wappsto.iot.rpc.model.from.server;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RpcStateCommand extends RpcData {
    @JsonProperty public Methods method;
    @JsonProperty public JsonRPCRequestFromServerParams params;
}
