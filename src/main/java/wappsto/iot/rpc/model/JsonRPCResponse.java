package wappsto.iot.rpc.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonRPCResponse extends JsonRPCMessage{
    @JsonProperty public JsonRPCResult result;
}
