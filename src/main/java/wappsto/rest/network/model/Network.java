package wappsto.rest.network.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Network {
    @JsonProperty
    public String id;
}
