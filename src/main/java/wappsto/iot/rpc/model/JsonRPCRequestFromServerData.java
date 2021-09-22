package wappsto.iot.rpc.model;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.model.schema.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonRPCRequestFromServerData {
    @JsonProperty String data;
    @JsonProperty Meta meta;
}
