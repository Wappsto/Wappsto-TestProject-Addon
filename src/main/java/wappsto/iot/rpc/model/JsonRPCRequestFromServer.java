package wappsto.iot.rpc.model;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonRPCRequestFromServer extends JsonRPCMessage{
    @JsonProperty public Methods method;
    public JsonRPCRequestFromServerParams params;
}
