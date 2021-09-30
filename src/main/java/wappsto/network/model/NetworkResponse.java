package wappsto.network.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkResponse {
    @JsonProperty public NetworkMeta meta;
}
