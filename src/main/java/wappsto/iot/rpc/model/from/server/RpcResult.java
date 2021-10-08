package wappsto.iot.rpc.model.from.server;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RpcResult extends RpcData {
    @JsonProperty public RpcResultData result;
}
