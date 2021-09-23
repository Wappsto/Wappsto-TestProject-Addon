package wappsto.iot.rpc.model.from.server;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.model.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonRPCRequestFromServerData {
    @JsonProperty String data;
    @JsonProperty
    Meta meta;
}
