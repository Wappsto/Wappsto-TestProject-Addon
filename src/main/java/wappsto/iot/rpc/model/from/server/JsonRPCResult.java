package wappsto.iot.rpc.model.from.server;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonRPCResult {
    @JsonProperty public boolean value;
}
