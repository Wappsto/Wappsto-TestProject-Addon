package wappsto.api.network.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StateResponse {
    @JsonProperty public String data;
}
