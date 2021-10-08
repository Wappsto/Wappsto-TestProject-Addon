package wappsto.iot.rpc.model.schema.incoming;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RpcStateCommand extends RpcData {
    @JsonProperty public Methods method;
    @JsonProperty public RpcCommandParams params;
}
