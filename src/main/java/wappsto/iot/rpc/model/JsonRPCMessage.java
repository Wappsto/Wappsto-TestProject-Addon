package wappsto.iot.rpc.model;

import com.fasterxml.jackson.annotation.*;

public abstract class JsonRPCMessage {
    @JsonProperty public String id;
}
