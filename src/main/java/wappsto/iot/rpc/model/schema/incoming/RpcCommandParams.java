package wappsto.iot.rpc.model.schema.incoming;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RpcCommandParams {
    @JsonProperty public RpcCommandData data;
}
