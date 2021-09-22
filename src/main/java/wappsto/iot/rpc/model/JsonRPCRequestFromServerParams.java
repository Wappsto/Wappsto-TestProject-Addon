package wappsto.iot.rpc.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonRPCRequestFromServerParams {
    @JsonProperty JsonRPCRequestFromServerData data;
}
