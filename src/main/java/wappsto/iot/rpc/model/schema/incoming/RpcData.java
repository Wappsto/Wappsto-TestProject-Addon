package wappsto.iot.rpc.model.schema.incoming;

import com.fasterxml.jackson.annotation.*;

public abstract class RpcData {
    @JsonProperty public String id;
}
