package wappsto.iot.rpc.model.from.server;

import com.fasterxml.jackson.annotation.*;

public abstract class JsonRpcMessage {
    @JsonProperty public String id;
}
