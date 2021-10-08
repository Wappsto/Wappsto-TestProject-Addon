package wappsto.iot.rpc.model.schema.incoming;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.model.schema.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RpcStateCommand extends RpcBase {
    @JsonProperty public Methods method;
    @JsonProperty public RpcCommandParams params;
}
