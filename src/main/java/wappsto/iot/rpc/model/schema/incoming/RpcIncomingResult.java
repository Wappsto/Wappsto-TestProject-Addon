package wappsto.iot.rpc.model.schema.incoming;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.model.schema.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RpcIncomingResult extends RpcBase {
    @JsonProperty public RpcResultData result;
}
