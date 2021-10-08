package wappsto.iot.rpc.model.from.server;

import com.fasterxml.jackson.annotation.*;

public abstract class RpcData {
    @JsonProperty public String id;
}
