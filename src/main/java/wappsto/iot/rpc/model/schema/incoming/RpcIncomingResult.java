package wappsto.iot.rpc.model.schema.incoming;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RpcIncomingResult extends RpcData {
    @JsonProperty public RpcResultData result;
}
